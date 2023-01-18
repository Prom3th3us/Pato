package kafka

import akka.actor.ActorSystem
import akka.kafka._
import akka.kafka.scaladsl.{Committer, Consumer, Producer}
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import akka.stream.{KillSwitches, UniqueKillSwitch}
import akka.{Done, NotUsed}
import com.lightbend.cinnamon.akka.stream.CinnamonAttributes
import com.lightbend.cinnamon.akka.stream.CinnamonAttributes.{GraphWithInstrumented, SourceWithInstrumented}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class KafkaCommittablePartitionedMessageProcessor(
    transactionRequirements: KafkaMessageProcessorRequirements
) extends MessageProcessor {

  type CommitableMsg = ConsumerMessage.CommittableMessage[String, String]

  //Creates a single switch that is used to control stream completion(akka-stream-graph)
  override type MessageProcessorKillSwitch = akka.stream.UniqueKillSwitch

  //Logger
  private val log = LoggerFactory.getLogger(this.getClass)

  //Setup Execution context
  implicit val ec: ExecutionContext = transactionRequirements.executionContext

  //Load configuration
  private val config = ConfigFactory.load()

  //Instance a configuration for Kafka
  private val appConfig = new KafkaConfig(config)

  //Function that returns UUID used for a Kafka configuration
  //Todo: remove if not used
  def transactionalId: String = java.util.UUID.randomUUID().toString

  final def run(
      SOURCE_TOPIC: String,
      SINK_TOPIC: String,
      RETRY_TOPIC: String,
      ERROR_TOPIC: String,
      algorithm: String => Future[Seq[String]]
  ): (Option[MessageProcessorKillSwitch], Future[Done]) = {

    //Counter created to monitor the number of messages processed
    val ProcessedMessagesCounter = transactionRequirements.monitoring.counter(
      s"$SOURCE_TOPIC-ProcessedMessagesCounter"
    )

    //Counter created to monitor the number of rejected messages
    val RejectedMessagesCounter = transactionRequirements.monitoring.counter(
      s"$SOURCE_TOPIC-RejectedMessagesCounter"
    )

    //Obtain the actor system through the requirements
    implicit val system: ActorSystem = transactionRequirements.system

    //Obtain the ConsumerSettings through the requirements and then configure the consumer group
    val consumerSetting: ConsumerSettings[String, String] =
      transactionRequirements.consumer.withGroupId(appConfig.CONSUMER_GROUP)

    //Topic Subscription
    val subscription: AutoSubscription =
      Subscriptions
        .topics(SOURCE_TOPIC)
        .withRebalanceListener(transactionRequirements.rebalancerListener)

    //Configure the number of partitions of the kafka consumer
    val NR_PARTITIONS: Int = appConfig.PARTITIONS_NUMBER

    //Configure the parallelism of the kafka consumer
    val CONSUMER_PARALLELISM: Int = Try(System.getenv("CONSUMER_PARALLELISM")).map(_.toInt).getOrElse(1)

    //Set up kafka committer settings
    val committerSettings: CommitterSettings = CommitterSettings(system)

    //Configuration for the kafka producer
    val config: Config = system.settings.config.getConfig("akka.kafka.producer")

    //Configuration the kafka producer
    val producerSettings: ProducerSettings[String, String] =
      ProducerSettings(config, new StringSerializer, new StringSerializer)

    //Configuration for committable partitioned source
    val commitableSource = Consumer.committablePartitionedSource(consumerSetting, subscription)

    /*We build a Runnable Graph to consume from kafka
     * A tuple of a UniqueKillSwitch is returned in case of wanting to interrupt
     *the stream and a Future[Done] in case of wanting to know if the operation was correct.*/
    val consumerGroupGraph: RunnableGraph[(UniqueKillSwitch, Future[Done])] =
      commitableSource
      //.buffer(10000, OverflowStrategy.backpressure)
        .mapAsyncUnordered(NR_PARTITIONS) {
          case (topicPartition, source: Source[CommitableMsg, NotUsed]) =>
            source
            //.buffer(CONSUMER_PARALLELISM * NR_PARTITIONS, OverflowStrategy.backpressure)
              .addAttributes( /* Instrument with cinammon */
                CinnamonAttributes.instrumented(reportByName = true,
                                                perFlow = true,
                                                perConnection = true,
                                                perBoundary = true,
                                                traceable = true)
              )
              .mapAsync(CONSUMER_PARALLELISM) { message: CommitableMsg =>
                val input: String = message.record.value

                log.debug(message.record.value) /*Log the record value */
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
                  //log.error(cause)
                  RejectedMessagesCounter.increment()
                  val output = Seq(message.record.value)
                  if (cause.contains("AskTimeoutException")){
                  //  log.error("Retrying due to AskTimeoutException -->" + message.record.key)
                    ProducerMessage.multi(
                      records = output.map { o =>
                        new ProducerRecord(
                          RETRY_TOPIC,
                          message.record.key,
                          o
                        )
                      }.toList,
                      passThrough = message.committableOffset
                    )
                  } else {
                    ProducerMessage.multi(
                      records = output.map { o =>
                        new ProducerRecord(
                          ERROR_TOPIC,
                          message.record.key,
                          o
                        )
                      }.toList,
                      passThrough = message.committableOffset
                    )
                  }

                case Right((message, output)) =>
                  ProcessedMessagesCounter.increment()
                  ProducerMessage.multi(
                    records = output.map { o =>
                      new ProducerRecord(
                        SOURCE_TOPIC + "_success",
                        message.record.key,
                        o
                      )
                    }.empty,
                    passThrough = message.committableOffset                  )
              }
              .via(Producer.flexiFlow(producerSettings))
              .map(_.passThrough)
              //       .collect {
              //         case a: ProducerMessage.Envelope[_, String, _] =>
              //            a.parts.map(a => a.record.value)
              //        }
              .instrumentedRunWith(Committer.sink(committerSettings))(name = "CommittablePartitioned",
                                                                      perFlow = true,
                                                                      perConnection = true,
                                                                      perBoundary = true)
        }
        .viaMat(KillSwitches.single)(Keep.right)
        .withAttributes(akka.defaultSupervisionStrategy)
        .toMat(Sink.ignore)(Keep.both)
        .instrumented(name = "CommittablePartitioned-Ext", perFlow = true, perConnection = true, perBoundary = true)

    val (killSwitch, done) = consumerGroupGraph.run()

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
