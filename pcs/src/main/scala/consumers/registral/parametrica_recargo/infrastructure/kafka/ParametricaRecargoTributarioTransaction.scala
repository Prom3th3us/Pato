package consumers.registral.parametrica_recargo.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.ParametricaRecargoTri
import consumers.registral.parametrica_recargo.infrastructure.dependency_injection.ParametricaRecargoActor
import consumers.registral.parametrica_recargo.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class ParametricaRecargoTributarioTransaction(actor: ParametricaRecargoActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ParametricaRecargoTri](monitoring) {
  def topic = "DGR-COP-PARAMRECARGO-TRI"
  def topicRetry = "DGR-COP-PARAMRECARGO-TRI_retry"
  def topicError = "DGR-COP-PARAMRECARGO-TRI_error"

  def processInput(input: String): Either[Throwable, ParametricaRecargoTri] =
    maybeDecode[ParametricaRecargoTri](input)

  override def processMessage(registro: ParametricaRecargoTri): Future[Response.SuccessProcessing] = {
    val command = ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto(
      parametricaRecargoId = registro.BPR_INDICE,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }
}
