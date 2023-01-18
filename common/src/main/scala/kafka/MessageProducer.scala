package kafka

import scala.concurrent.Future
import akka.Done
import kafka.KafkaMessageProducer.KafkaKeyValue

/*
This mechanism allows the user to publish message to the message bus

It delegates onto the user the serialization of the messages
 */

trait MessageProducer {
  def createTopic(topic: String): Future[Done]
  def produce(data: Seq[KafkaKeyValue], topic: String)(handler: Seq[KafkaKeyValue] => Unit): Future[Done]
}
