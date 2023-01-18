package design_principles.actor_model

import design_principles.actor_model.testkit.QueryTestkit
import design_principles.external_pub_sub.kafka.MessageProcessorLogging
import kafka.{MessageProcessor, MessageProducer}

trait ActorSpecWriteside {
  def Query: QueryTestkit

  def messageProducer: MessageProducer
  def messageProcessor: MessageProcessor with MessageProcessorLogging
}
