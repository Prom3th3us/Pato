package consumers.registral.parametrica_plan.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanCommands
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto.ParametricaPlanAnt
import consumers.registral.parametrica_plan.infrastructure.dependency_injection.ParametricaPlanActor
import consumers.registral.parametrica_plan.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class ParametricaPlanNoTributarioTransaction(actor: ParametricaPlanActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ParametricaPlanAnt](monitoring) {
  def topic = "DGR-COP-PARAMPLAN-ANT"
  def topicRetry = "DGR-COP-PARAMPLAN-ANT_retry"
  def topicError = "DGR-COP-PARAMPLAN-ANT_error"

  def processInput(input: String): Either[Throwable, ParametricaPlanAnt] =
    maybeDecode[ParametricaPlanAnt](input)

  override def processMessage(registro: ParametricaPlanAnt): Future[Response.SuccessProcessing] = {
    val command = ParametricaPlanCommands.ParametricaPlanUpdateFromDto(
      parametricaPlanId = registro.BPP_FPM_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
