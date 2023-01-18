package consumers_spec.no_registrales.obligacion.acceptance

import akka.actor.ActorSystem
import akka.entity.ShardedEntity.ShardedEntityRequirements
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.obligacion.ObligacionSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock
import org.scalatest.Ignore

object ObligacionSpecAcceptance {
  def getContext(system: ActorSystem): ObligacionSpec.TestContext = {
    val ObligacionSpecMessageBroker = new KafkaMock()
    val ObligacionSpecQuery = {
      val sujetoActor =
        SujetoActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.production(system)
          )(system)
      val cotitularidadActor =
        CotitularidadActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.production(system)
          )(system)
      new MessageTestkitUtils(sujetoActor, cotitularidadActor)
        .StartMessageProcessor(ObligacionSpecMessageBroker)
        .startProcessing()
      new NoRegistralesQueryWithActorRef(
        sujetoActor
      )
    }
    ObligacionSpec TestContext (
      messageProducer = ObligacionSpecMessageBroker,
      messageProcessor = ObligacionSpecMessageBroker,
      Query = ObligacionSpecQuery
    )
  }
}

@Ignore
class ObligacionSpecAcceptance
    extends ObligacionSpec(
      ObligacionSpecAcceptance.getContext
    )
