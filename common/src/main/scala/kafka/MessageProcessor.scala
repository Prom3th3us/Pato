package kafka

import scala.concurrent.Future
import akka.Done
import akka.stream.KillSwitch

/*
This mechanism allows the user to process messages from the message bus

It delegates onto the user the deserialization of the messages

It returns a tuple which contains

1. KillSwitch for the user to stop the message stream
2. Future[Done] to handle the correct stream termination
 */
trait MessageProcessor {

  type MessageProcessorKillSwitch <: KillSwitch
  def run(
      SOURCE_TOPIC: String,
      SINK_TOPIC: String,
      RETRY_TOPIC: String,
      ERROR_TOPIC: String,
      algorithm: String => Future[Seq[String]]
  ): (Option[MessageProcessorKillSwitch], Future[Done])

}
