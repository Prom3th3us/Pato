package consumers.registral.tramite.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.tramite.application.entities.TramiteCommands
import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import consumers.registral.tramite.infrastructure.dependency_injection.TramiteActor
import consumers.registral.tramite.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class TramiteTransaction(actor: TramiteActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[Tramite](monitoring) {
  def topic = "DGR-COP-TRAMITES"
  def topicRetry = "DGR-COP-TRAMITES_retry"
  def topicError = "DGR-COP-TRAMITES_error"

  def processInput(input: String): Either[Throwable, Tramite] =
    maybeDecode[Tramite](input)

  override def processMessage(registro: Tramite): Future[Response.SuccessProcessing] = {
    val command = TramiteCommands.TramiteUpdateFromDto(
      sujetoId = registro.BTR_SUJ_IDENTIFICADOR,
      tramiteId = registro.BTR_TRMID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
