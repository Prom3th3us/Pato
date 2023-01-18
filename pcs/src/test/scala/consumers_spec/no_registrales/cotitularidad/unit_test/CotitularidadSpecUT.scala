package consumers_spec.no_registrales.cotitularidad.unit_test

import akka.actor.ActorSystem
import akka.entity.ShardedEntity.{MonitoringAndMessageProducer, ShardedEntityRequirements}
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.ConfigFactory
import consumers.no_registral.cotitularidad.application.entities.CotitularidadCommands.CotitularidadPublishSnapshot
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.cotitularidad.infrastructure.dependency_injection.CotitularidadActor
import consumers.no_registral.cotitularidad.infrastructure.kafka.ObjetoSnapshotPersistedHandler
import consumers.no_registral.objeto.infrastructure.consumer.{
  ObjetoTributarioTransaction,
  ObjetoUpdateCotitularesTransaction,
  ObjetoUpdateNovedadTransaction
}
import consumers.no_registral.obligacion.infrastructure.consumer.ObligacionTributariaTransaction
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers_spec.no_registrales.cotitularidad.CotitularidadSpec
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryWithActorRef
import consumers_spec.no_registrales.testkit.{MessageTestkitUtils, MonitoringAndMessageProducerMock}
import design_principles.external_pub_sub.kafka.KafkaMock

object CotitularidadSpecUT {
  def getContext(system: ActorSystem): CotitularidadSpec.TestContext = {
    val CotitularidadSpecMessageBroker = new KafkaMock()
    val CotitularidadSpecQuery = {
      val sujetoActor =
        SujetoActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.dummy.copy(
              messageProducer = CotitularidadSpecMessageBroker
            )
          )(system)
      val cotitularidadActor =
        CotitularidadActor
          .startWithRequirements(
            MonitoringAndMessageProducerMock.dummy.copy(
              messageProducer = CotitularidadSpecMessageBroker
            )
          )(system)
      new MessageTestkitUtils(sujetoActor, cotitularidadActor)
        .StartMessageProcessor(CotitularidadSpecMessageBroker)
        .startProcessing {
          implicit val actorTransactionRequirements: ActorTransactionRequirements = ActorTransactionRequirements(
            executionContext = scala.concurrent.ExecutionContext.Implicits.global,
            config = ConfigFactory.empty
          )
          val monitoring = MonitoringAndMessageProducerMock.dummy.monitoring
          Set(
            ObjetoSnapshotPersistedHandler(cotitularidadActor, monitoring),
            ObjetoUpdateCotitularesTransaction(sujetoActor, monitoring),
            ObjetoTributarioTransaction(sujetoActor, monitoring),
            ObligacionTributariaTransaction(sujetoActor, monitoring),
            ObjetoUpdateNovedadTransaction(sujetoActor, monitoring)
          )
        }
      new NoRegistralesQueryWithActorRef(
        sujetoActor
      )
    }
    CotitularidadSpec TestContext (
      messageProducer = CotitularidadSpecMessageBroker,
      messageProcessor = CotitularidadSpecMessageBroker,
      Query = CotitularidadSpecQuery
    )
  }
}

class CotitularidadSpecUT
    extends CotitularidadSpec(
      CotitularidadSpecUT.getContext
    )
