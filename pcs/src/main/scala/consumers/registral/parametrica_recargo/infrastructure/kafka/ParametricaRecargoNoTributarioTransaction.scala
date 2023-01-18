package consumers.registral.parametrica_recargo.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.ParametricaRecargoAnt
import consumers.registral.parametrica_recargo.infrastructure.dependency_injection.ParametricaRecargoActor
import consumers.registral.parametrica_recargo.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class ParametricaRecargoNoTributarioTransaction(actor: ParametricaRecargoActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ParametricaRecargoAnt](monitoring) {
  def topic = "DGR-COP-PARAMRECARGO-ANT"
  def topicRetry = "DGR-COP-PARAMRECARGO-ANT_retry"
  def topicError = "DGR-COP-PARAMRECARGO-ANT_error"

  def processInput(input: String): Either[Throwable, ParametricaRecargoAnt] =
    maybeDecode[ParametricaRecargoAnt](input)

  override def processMessage(registro: ParametricaRecargoAnt): Future[Response.SuccessProcessing] = {

    val command = ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto(
      parametricaRecargoId = registro.BPR_INDICE,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
