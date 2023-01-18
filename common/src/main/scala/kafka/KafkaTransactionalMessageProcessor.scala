package kafka

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Transactional
import akka.kafka.{ConsumerMessage, ProducerMessage, Subscriptions}
import akka.stream.KillSwitches
import akka.stream.scaladsl.{Keep, Sink}
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

class KafkaTransactionalMessageProcessor(
    transactionRequirements: KafkaMessageProcessorRequirements
) extends MessageProcessor {

  override type MessageProcessorKillSwitch = akka.stream.UniqueKillSwitch

  private val log = LoggerFactory.getLogger(this.getClass)
  implicit val ec: ExecutionContext = transactionRequirements.executionContext

  def transactionalId: String = java.util.UUID.randomUUID().toString

  def run(
      SOURCE_TOPIC: String,
      SINK_TOPIC: String,
      RETRY_TOPIC: String,
      ERROR_TOPIC: String,
      algorithm: String => Future[Seq[String]]
  ): (Option[MessageProcessorKillSwitch], Future[Done]) = {

    val ProcessedMessagesCounter = transactionRequirements.monitoring.counter(
      s"$SOURCE_TOPIC-ProcessedMessagesCounter"
    )
    val RejectedMessagesCounter = transactionRequirements.monitoring.counter(
      s"$SOURCE_TOPIC-RejectedMessagesCounter"
    )
    type Msg = ConsumerMessage.TransactionalMessage[String, String]

    implicit val system: ActorSystem = transactionRequirements.system
    val consumer = transactionRequirements.consumer
    val producer = transactionRequirements.producer
    val rebalancerListener = transactionRequirements.rebalancerListener

    val subscription = Subscriptions.topics(SOURCE_TOPIC).withRebalanceListener(rebalancerListener)

    /*
    30 particiones / 3 nodos = 10 particiones por nodo
      Actores creados con maxParallelism de 12 en su forkJoin
      10 * 12 = 120

      mapAsyncUnordered(120)

      comentario sobre mapAsyncUnordered:
      - utilizar particiones de Kafka es en sí mismo una violación del orden
      - una vez perdido el orden, no es necesario seguirse preocupando por el orden

     */

    val THROTTLE_ELEMENTS: Int = Try(System.getenv("THROTTLE_ELEMENTS")).map(_.toInt).getOrElse(1000)
    val THROTTLE_ELEMENTS_PER: Int = Try(System.getenv("THROTTLE_ELEMENTS_PER")).map(_.toInt).getOrElse(1)
    val CONSUMER_PARALLELISM: Int = Try(System.getenv("CONSUMER_PARALLELISM")).map(_.toInt).getOrElse(1024)

    val stream = Transactional
      .source(consumer, subscription) // TPS -- Transaction Per Second
      .throttle(THROTTLE_ELEMENTS, THROTTLE_ELEMENTS_PER second) // si vos buscas 3000 total, osea 200K, por pod, deberia ser 3000 / 3 = 1000
      .mapAsyncUnordered(CONSUMER_PARALLELISM) { msg: ConsumerMessage.TransactionalMessage[String, String] =>
        val message = msg

        val input: String = message.record.value

        log.debug(message.record.value)

        algorithm(input)
          .map { a: Seq[String] =>
            Right(message -> a)
          }
          .recover {
            case e: Exception =>
              Left(message -> s"""
                  Error in flow:
                  ${e.getMessage}
                  For input:
                  $input
              """)
          }
      }
      .map {

        case Left((message, cause)) =>
          log.error(cause)
          RejectedMessagesCounter.increment()
          val output = Seq(message.record.value)
          ProducerMessage.multi(
            records = output.map { o =>
              new ProducerRecord(
                RETRY_TOPIC,
                message.record.key,
                o
              )
            }.toList,
            passThrough = message.partitionOffset
          )
        case Right((message, output)) =>
          ProcessedMessagesCounter.increment()
          ProducerMessage.multi(
            records = output.map { o =>
              new ProducerRecord(
                SINK_TOPIC,
                message.record.key,
                o
              )
            }.toList,
            passThrough = message.partitionOffset
          )
      }
      .via(Transactional.flow(producer, transactionalId))
      .viaMat(KillSwitches.single)(Keep.right)
      .collect {
        case a: ProducerMessage.MultiResult[_, String, _] =>
          a.parts.map(a => a.record.value)
      }
      .withAttributes(akka.defaultSupervisionStrategy)
      .toMat(Sink.ignore)(Keep.both)

    val (killSwitch, done) = stream.run()

    done.onComplete {
      case Success(_) =>
        log.warn(s"""
                    |     Stream completed with success
                    |     This is caused by the HTTP endpoint /kafka/stop/$SOURCE_TOPIC
                    |     Because of this we will take no action to interfere:
                    |     The topic will not be restarted on it's own.
          """.stripMargin)
        killSwitch.shutdown()
      case Failure(ex) =>
        log.error(s"Stream completed with failure -- ${ex.getMessage}")
        killSwitch.shutdown()
        run(SOURCE_TOPIC, SINK_TOPIC, RETRY_TOPIC, ERROR_TOPIC, algorithm)
    }
    (Some(killSwitch), done)
  }
}
