package consumers_spec.no_registrales.sujeto.unit_test

import akka.actor.ActorSystem
import akka.entity.ShardedEntity.ShardedEntityRequirements
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.sujeto.SujetoSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock
import org.scalatest.Ignore

object SujetoSpecAcceptance {
  def getContext(system: ActorSystem): SujetoSpec.TestContext = {
    val SujetoSpecMessageBroker = new KafkaMock()
    val SujetoSpecQuery = {
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
        .StartMessageProcessor(SujetoSpecMessageBroker)
        .startProcessing()
      new NoRegistralesQueryWithActorRef(
        sujetoActor
      )
    }
    SujetoSpec TestContext (
      messageProducer = SujetoSpecMessageBroker,
      messageProcessor = SujetoSpecMessageBroker,
      Query = SujetoSpecQuery
    )
  }
}

@Ignore
class SujetoSpecAcceptance
    extends SujetoSpec(
      SujetoSpecAcceptance.getContext
    )
