package consumers_spec.no_registrales.testkit.mocks

import akka.actor.{ActorSystem, Props}
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import consumers_spec.no_registrales.testkit.MonitoringAndMessageProducerMock
import monitoring.Monitoring

class ObjetoActorWithMockPersistence(
    dummy: MonitoringAndMessageProducerMock,
    actorTransactionRequirements: ActorTransactionRequirements
)(implicit system: ActorSystem) {

  def props(dummy: MonitoringAndMessageProducerMock): Props = {
    val obligacionProps = new ObligacionActorWithMockPersistence().props(dummy)
    Props(
      new ObjetoActor(dummy, Some(obligacionProps))
    )
  }
}
