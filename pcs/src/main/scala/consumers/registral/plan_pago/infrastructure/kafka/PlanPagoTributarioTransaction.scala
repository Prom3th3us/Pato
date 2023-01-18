package consumers.registral.plan_pago.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.plan_pago.application.entities.PlanPagoCommands
import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto.PlanPagoTri
import consumers.registral.plan_pago.infrastructure.dependency_injection.PlanPagoActor
import consumers.registral.plan_pago.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class PlanPagoTributarioTransaction(actor: PlanPagoActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[PlanPagoTri](monitoring) {
  def topic = "DGR-COP-PLANES-TRI"
  def topicRetry = "DGR-COP-PLANES-TRI_retry"
  def topicError = "DGR-COP-PLANES-TRI_error"

  def processInput(input: String): Either[Throwable, PlanPagoTri] =
    maybeDecode[PlanPagoTri](input)

  override def processMessage(registro: PlanPagoTri): Future[Response.SuccessProcessing] = {
    val command = PlanPagoCommands.PlanPagoUpdateFromDto(
      sujetoId = registro.BPL_SUJ_IDENTIFICADOR,
      objetoId = registro.BPL_SOJ_IDENTIFICADOR,
      tipoObjeto = registro.BPL_SOJ_TIPO_OBJETO,
      planPagoId = registro.BPL_PLN_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }
}
