package design_principles.actor_model.testkit

import design_principles.actor_model.{ActorSpec, ActorSpecWriteside}
import design_principles.external_pub_sub.kafka.{KafkaMock, MessageProcessorLogging}
import design_principles.projection.mock.CassandraTestkitMock

trait MockTestkit { _: ActorSpec with ActorSpecWriteside =>
  def kafkaMock: KafkaMock

  val messageProducer: KafkaMock = kafkaMock
  def messageProcessor: KafkaMock with MessageProcessorLogging = kafkaMock

  def cassandraTestkit: CassandraTestkitMock
}
