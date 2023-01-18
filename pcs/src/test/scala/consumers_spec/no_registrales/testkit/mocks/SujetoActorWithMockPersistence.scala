package consumers_spec.no_registrales.testkit.mocks

import akka.actor.{ActorSystem, Props}
import akka.entity.ShardedEntity
import akka.entity.ShardedEntity.ShardedEntityNoRequirements
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import config.MockConfig
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.testkit.MonitoringAndMessageProducerMock
import kafka.MessageProducer
import monitoring.{DummyMonitoring, Monitoring}

class SujetoActorWithMockPersistence(
    dummy: MonitoringAndMessageProducerMock
)(implicit
  system: ActorSystem,
  actorTransactionRequirements: ActorTransactionRequirements)
    extends ShardedEntityNoRequirements {

  override def props(requirements: ShardedEntity.NoRequirements): Props = {
    val objetoProps = new ObjetoActorWithMockPersistence(
      dummy,
      actorTransactionRequirements
    ).props(dummy)
    Props(
      new SujetoActor(
        dummy,
        Some(objetoProps)
      )
    )
  }
}
