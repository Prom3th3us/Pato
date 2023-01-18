package consumers.no_registral.objeto.infrastructure.consumer

import scala.concurrent.{ExecutionContext, Future}
import akka.Done
import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{Exencion, ObjetosAnt}
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.Response
import monitoring.Monitoring
import serialization.{decodeF, maybeDecode}

import scala.util.Try

case class ObjetoNoTributarioTransaction(actorRef: ActorRef, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ObjetosAnt](monitoring) {

  def topic = "DGR-COP-OBJETOS-ANT"
  def topicRetry = "DGR-COP-OBJETOS-ANT_retry"
  def topicError = "DGR-COP-OBJETOS-ANT_error"


  def processInput(input: String): Either[Throwable, ObjetosAnt] =
    maybeDecode[ObjetosAnt](input)

  def processMessage(registro: ObjetosAnt): Future[Response.SuccessProcessing] = {
    val command: ObjetoCommands =
      if (registro.SOJ_ESTADO.contains("BAJA"))
        ObjetoCommands.SetBajaObjeto(
          sujetoId = registro.SOJ_SUJ_IDENTIFICADOR,
          objetoId = registro.SOJ_IDENTIFICADOR,
          tipoObjeto = registro.SOJ_TIPO_OBJETO,
          deliveryId = registro.EV_ID,
          registro = registro,
          isResponsable = None,
          sujetoResponsable = None
        )
      else
        ObjetoCommands.ObjetoUpdateFromAnt(
          sujetoId = registro.SOJ_SUJ_IDENTIFICADOR,
          objetoId = registro.SOJ_IDENTIFICADOR,
          tipoObjeto = registro.SOJ_TIPO_OBJETO,
          deliveryId = registro.EV_ID,
          registro = registro
        )

    actorRef.ask[Response.SuccessProcessing](command)
  }
}
