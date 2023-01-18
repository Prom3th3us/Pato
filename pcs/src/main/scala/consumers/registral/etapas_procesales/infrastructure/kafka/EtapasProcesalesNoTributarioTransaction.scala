package consumers.registral.etapas_procesales.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesCommands
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto.EtapasProcesalesAnt
import consumers.registral.etapas_procesales.infrastructure.dependency_injection.EtapasProcesalesActor
import consumers.registral.etapas_procesales.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class EtapasProcesalesNoTributarioTransaction(actor: EtapasProcesalesActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[EtapasProcesalesAnt](monitoring) {
  def topic = "DGR-COP-ETAPROCESALES-ANT"
  def topicRetry = "DGR-COP-ETAPROCESALES-ANT_retry"
  def topicError = "DGR-COP-ETAPROCESALES-ANT_error"

  def processInput(input: String): Either[Throwable, EtapasProcesalesAnt] =
    maybeDecode[EtapasProcesalesAnt](input)

  override def processMessage(registro: EtapasProcesalesAnt): Future[Response.SuccessProcessing] = {
    val command = EtapasProcesalesCommands.EtapasProcesalesUpdateFromDto(
      juicioId = registro.BEP_JUI_ID,
      etapaId = registro.BPE_ETA_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
