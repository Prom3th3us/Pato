package design_principles.actor_model.mechanism.stream_supervision

import design_principles.actor_model.mechanism.stream_supervision.MessageProcessorSupervisorActorRequirements.TopicAndAlgorithm
import kafka.MessageProcessor

import scala.concurrent.Future

object MessageProcessorSupervisorActorRequirements {

  case class TopicAndAlgorithm(
      SOURCE_TOPIC: String,
      SINK_TOPIC: String,
      algorithm: String => Future[Seq[String]]
  )
}

case class MessageProcessorSupervisorActorRequirements(
    messageProcessorRequirements: TopicAndAlgorithm,
    messageProcessor: MessageProcessor
)
