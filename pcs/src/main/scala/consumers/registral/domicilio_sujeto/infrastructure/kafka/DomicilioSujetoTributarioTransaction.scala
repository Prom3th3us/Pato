package consumers.registral.domicilio_sujeto.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoCommands
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.DomicilioSujetoTri
import consumers.registral.domicilio_sujeto.infrastructure.dependency_injection.DomicilioSujetoActor
import consumers.registral.domicilio_sujeto.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class DomicilioSujetoTributarioTransaction(actor: DomicilioSujetoActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[DomicilioSujetoTri](monitoring) {
  def topic = "DGR-COP-DOMICILIO-SUJ-TRI"
  def topicRetry = "DGR-COP-DOMICILIO-SUJ-TRI_retry"
  def topicError = "DGR-COP-DOMICILIO-SUJ-TRI_error"

  def processInput(input: String): Either[Throwable, DomicilioSujetoTri] =
    maybeDecode[DomicilioSujetoTri](input)

  override def processMessage(registro: DomicilioSujetoTri): Future[Response.SuccessProcessing] = {
    val command = DomicilioSujetoCommands.DomicilioSujetoUpdateFromDto(
      sujetoId = registro.BDS_SUJ_IDENTIFICADOR,
      domicilioId = registro.BDS_DOM_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }
}
