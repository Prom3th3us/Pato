package consumers_spec.no_registrales.sujeto.unit_test

import akka.actor.ActorSystem
import akka.entity.ShardedEntity.ShardedEntityRequirements
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.sujeto.SujetoSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock

object SujetoSpecUT {
  def getContext(system: ActorSystem): SujetoSpec.TestContext = {
    val SujetoSpecMessageBroker = new KafkaMock()
    val SujetoSpecQueryAgainstActors = {
      val sujetoActor =
        SujetoActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.dummy.copy(
              messageProducer = SujetoSpecMessageBroker
            )
          )(system)
      val cotitularidadActor =
        CotitularidadActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.dummy.copy(
              messageProducer = SujetoSpecMessageBroker
            )
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
      Query = SujetoSpecQueryAgainstActors
    )
  }
}

class SujetoSpecUT
    extends SujetoSpec(
      SujetoSpecUT getContext
    )
