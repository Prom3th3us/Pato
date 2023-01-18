package consumers_spec.no_registrales.cotitularidad.acceptance

import akka.actor.ActorSystem
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.ConfigFactory
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.cotitularidad.infrastructure.kafka.ObjetoSnapshotPersistedHandler
import consumers.no_registral.objeto.infrastructure.consumer.{
  ObjetoTributarioTransaction,
  ObjetoUpdateCotitularesTransaction
}
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.cotitularidad.CotitularidadSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock
import org.scalatest.Ignore

object CotitularidadSpecAcceptance {
  def getContext(system: ActorSystem): CotitularidadSpec.TestContext = {
    val CotitularidadSpecMessageBroker = new KafkaMock()
    val CotitularidadSpecQuery = {
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
        .StartMessageProcessor(CotitularidadSpecMessageBroker)
        .startProcessing {
          implicit val actorTransactionRequirements: ActorTransactionRequirements = ActorTransactionRequirements(
            executionContext = scala.concurrent.ExecutionContext.Implicits.global,
            config = ConfigFactory.empty
          )
          val monitoring = MonitoringAndMessageProducerMock.production(system).monitoring
          Set(
            ObjetoSnapshotPersistedHandler(cotitularidadActor, monitoring),
            ObjetoUpdateCotitularesTransaction(sujetoActor, monitoring),
            ObjetoTributarioTransaction(sujetoActor, monitoring)
          )
        }
      new NoRegistralesQueryWithActorRef(
        cotitularidadActor
      )
    }
    CotitularidadSpec TestContext (
      messageProducer = CotitularidadSpecMessageBroker,
      messageProcessor = CotitularidadSpecMessageBroker,
      Query = CotitularidadSpecQuery
    )
  }
}

@Ignore
class CotitularidadSpecAcceptance
    extends CotitularidadSpec(
      CotitularidadSpecAcceptance.getContext
    )
