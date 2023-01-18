package consumers.no_registral.sujeto.infrastructure.consumer

import scala.concurrent.Future
import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.{SujetoAnt, SujetoTri}
import consumers.no_registral.sujeto.application.entity.{SujetoCommands, SujetoExternalDto}
import consumers.no_registral.sujeto.infrastructure.json._
import design_principles.actor_model.Response
import monitoring.Monitoring
import oracle.Oracle.connOracleKafkaToWriteside
import serialization.maybeDecode

import scala.util.Try

case class SujetoTributarioTransaction(actorRef: ActorRef, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[SujetoTri](monitoring) {

  def topic = "DGR-COP-SUJETO-TRI"
  def topicRetry = "DGR-COP-SUJETO-TRI_retry"
  def topicError = "DGR-COP-SUJETO-TRI_error"

  def processInput(input: String): Either[Throwable, SujetoTri] =
    maybeDecode[SujetoTri](input)

  def processMessage(registro: SujetoTri): Future[Response.SuccessProcessing] = {
    //connOracleKafkaToWriteside(registro.EV_ID.toString(), "sujeto", registro.SUJ_CANAL_ORIGEN.getOrElse("TAX"))
    val command = registro match {
      case _: SujetoExternalDto.SujetoTri =>
        SujetoCommands.SujetoUpdateFromTri(
          sujetoId = registro.SUJ_IDENTIFICADOR,
          deliveryId = registro.EV_ID,
          registro = registro
        )
    }
    actorRef.ask[Response.SuccessProcessing](command)

  }
}
