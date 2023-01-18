package kafka

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import kafka.KafkaMessageProcessorRequirements.bootstrapServers
import kafka.KafkaMessageProducer.KafkaKeyValue
import monitoring.Monitoring
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

class KafkaMessageProducer()(
    implicit
    system: ActorSystem,
    producerSettings: ProducerSettings[String, String]
) extends MessageProducer {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def createTopic(topic: String): Future[Done] = Future.successful(Done)

  def produce(data: Seq[KafkaKeyValue], topic: String)(handler: Seq[KafkaKeyValue] => Unit): Future[Done] = {

    implicit val ec: ExecutionContextExecutor = system.getDispatcher

    val publication: Future[Done] = Source(data)
    // NOTE: If no partition is specified but a key is present a partition will be chosen
    // using a hash of the key. If neither key nor partition is present a partition
    // will be assigned in a round-robin fashion.
      .map { m =>
        new ProducerRecord[String, String](topic, m.aggregateRoot, m.json)
      }
      .runWith(Producer.plainSink(producerSettings))

    publication.onComplete {
      case Success(Done) =>
        data foreach { s =>
          log.debug(s"""Published $s to $topic""")

        }

        handler(data)
      case Failure(t) => log.error("An error has occurred: " + t.getMessage)
    }

    publication

  }
}

object KafkaMessageProducer {

  case class KafkaKeyValue(aggregateRoot: String, json: String) {
    def key = aggregateRoot
    def value = json
  }

  def apply(monitoring: Monitoring,
            rebalancerListener: ActorRef)(implicit system: ActorSystem): KafkaMessageProducer = {
    implicit def producerSettings: ProducerSettings[String, String] =
      ProducerSettings(system, new StringSerializer, new StringSerializer)
        .withBootstrapServers(bootstrapServers)
        .withProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "100663296") //TODO changed the buffer memory config to reduce the latency
        .withProperty(ProducerConfig.ACKS_CONFIG, "0")
        .withProperty(ProducerConfig.LINGER_MS_CONFIG, "5")
        .withProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG, "120000")
    new KafkaMessageProducer()
  }
}
