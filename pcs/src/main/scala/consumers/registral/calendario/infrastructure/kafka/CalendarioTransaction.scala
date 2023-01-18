package consumers.registral.calendario.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.calendario.application.entities.{CalendarioCommands, CalendarioExternalDto}
import consumers.registral.calendario.infrastructure.dependency_injection.CalendarioActor
import consumers.registral.calendario.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class CalendarioTransaction(actor: CalendarioActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[CalendarioExternalDto](monitoring) {
  def topic = "DGR-COP-CALENDARIO"
  def topicRetry = "DGR-COP-CALENDARIO_retry"
  def topicError = "DGR-COP-CALENDARIO_error"

  def processInput(input: String): Either[Throwable, CalendarioExternalDto] =
    maybeDecode[CalendarioExternalDto](input)

  override def processMessage(registro: CalendarioExternalDto): Future[Response.SuccessProcessing] = {
    val command = registro match {
      case registro: CalendarioExternalDto =>
        CalendarioCommands.CalendarioUpdateFromDto(
          calendarioId = registro.BCL_IDENTIFICADOR,
          deliveryId = BigInt(registro.EV_ID),
          registro = registro
        )
    }

    actor.ask(command)
  }

}
