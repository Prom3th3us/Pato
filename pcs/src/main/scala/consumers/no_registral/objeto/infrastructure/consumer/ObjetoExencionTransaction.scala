package consumers.no_registral.objeto.infrastructure.consumer

import scala.concurrent.{ExecutionContext, Future}
import akka.Done
import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.Exencion
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.Response
import monitoring.Monitoring
import serialization.{decodeF, maybeDecode}

import scala.util.Try

case class ObjetoExencionTransaction(actorRef: ActorRef, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[Exencion](monitoring) {

  def topic = "DGR-COP-EXENCIONES"
  def topicRetry = "DGR-COP-EXENCIONES_retry"
  def topicError = "DGR-COP-EXENCIONES_error"

  def processInput(input: String): Either[Throwable, Exencion] =
    maybeDecode[Exencion](input)

  def processMessage(exencion: Exencion): Future[Response.SuccessProcessing] = {
    val command = ObjetoCommands.ObjetoAddExencion(
      deliveryId = exencion.EV_ID,
      sujetoId = exencion.BEX_SUJ_IDENTIFICADOR,
      objetoId = exencion.BEX_SOJ_IDENTIFICADOR,
      tipoObjeto = exencion.BEX_SOJ_TIPO_OBJETO,
      exencion = exencion
    )

    actorRef.ask[Response.SuccessProcessing](command)
  }
}
