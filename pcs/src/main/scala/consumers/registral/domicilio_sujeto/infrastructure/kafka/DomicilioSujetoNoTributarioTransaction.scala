package consumers.registral.domicilio_sujeto.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoCommands
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.DomicilioSujetoAnt
import consumers.registral.domicilio_sujeto.infrastructure.dependency_injection.DomicilioSujetoActor
import consumers.registral.domicilio_sujeto.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class DomicilioSujetoNoTributarioTransaction(actor: DomicilioSujetoActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[DomicilioSujetoAnt](monitoring) {
  def topic = "DGR-COP-DOMICILIO-SUJ-ANT"
  def topicRetry = "DGR-COP-DOMICILIO-SUJ-ANT_retry"
  def topicError = "DGR-COP-DOMICILIO-SUJ-ANT_error"

  def processInput(input: String): Either[Throwable, DomicilioSujetoAnt] =
    maybeDecode[DomicilioSujetoAnt](input)

  override def processMessage(registro: DomicilioSujetoAnt): Future[Response.SuccessProcessing] = {
    val command = DomicilioSujetoCommands.DomicilioSujetoUpdateFromDto(
      sujetoId = registro.BDS_SUJ_IDENTIFICADOR,
      domicilioId = registro.BDS_DOM_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
