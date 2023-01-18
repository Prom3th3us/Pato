package consumers_spec.no_registrales.testkit.mocks

import akka.actor.Props
import config.MockConfig
import consumers.no_registral.obligacion.infrastructure.dependency_injection.ObligacionActor
import consumers_spec.no_registrales.testkit.MonitoringAndMessageProducerMock
import monitoring.{DummyMonitoring, Monitoring}

class ObligacionActorWithMockPersistence() {

  def props(dummy: MonitoringAndMessageProducerMock): Props =
    Props(new ObligacionActor(dummy))
}
