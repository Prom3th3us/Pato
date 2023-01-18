package consumers_spec.no_registrales.testkit.mocks

import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.entity.ShardedEntity
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import design_principles.actor_model.testkit.KafkaTestkit
import kafka.MessageProducer
import monitoring.Monitoring

import scala.concurrent.Future
import akka.actor.typed.scaladsl.adapter._
import consumers_spec.no_registrales.testkit.MonitoringAndMessageProducerMock

class CotitularidadActorWithMockPersistence()(implicit system: ActorSystem)
    extends ShardedEntity[MonitoringAndMessageProducerMock] {
  override def props(dumy: MonitoringAndMessageProducerMock): Props = Props(
    new CotitularidadActor(MonitoringAndMessageProducerMock.dummy)
  )
}
