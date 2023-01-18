package consumers_spec.no_registrales.objeto.acceptance

import akka.actor.ActorSystem
import akka.entity.ShardedEntity.ShardedEntityRequirements
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.objeto.ObjetoSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock
import org.scalatest.Ignore

object ObjetoSpecAcceptance {
  def getContext(system: ActorSystem): ObjetoSpec.TestContext = {
    val ObjetoSpecMessageBroker = new KafkaMock() // TODO implement createTopic for the real KafkaProducer and use it
    val ObjetoSpecQuery = {
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
        .StartMessageProcessor(ObjetoSpecMessageBroker)
        .startProcessing()
      new NoRegistralesQueryWithActorRef(
        sujetoActor
      )
    }
    ObjetoSpec TestContext (
      messageProducer = ObjetoSpecMessageBroker,
      messageProcessor = ObjetoSpecMessageBroker,
      Query = ObjetoSpecQuery
    )
  }
}

@Ignore
class ObjetoSpecAcceptance
    extends ObjetoSpec(
      ObjetoSpecAcceptance.getContext
    )
