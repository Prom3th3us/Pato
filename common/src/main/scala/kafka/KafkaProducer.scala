package kafka

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.util.{Failure, Success}

object KafkaProducer {

  def produce(data: Seq[String], topic: String)(handler: Seq[String] => Unit)(
      implicit
      system: ActorSystem,
      producerSettings: ProducerSettings[String, String]
  ): Future[Done] = {

    implicit val ec = system.getDispatcher

    val publication: Future[Done] = Source(data)
    // NOTE: If no partition is specified but a key is present a partition will be chosen
    // using a hash of the key. If neither key nor partition is present a partition
    // will be assigned in a round-robin fashion.
      .map(new ProducerRecord[String, String](topic, _))
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
  val log = LoggerFactory.getLogger(this.getClass)

}
