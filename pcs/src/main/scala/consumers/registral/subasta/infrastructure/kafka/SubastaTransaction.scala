package consumers.registral.subasta.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.subasta.application.entities.{SubastaCommands, SubastaExternalDto}
import consumers.registral.subasta.infrastructure.dependency_injection.SubastaActor
import consumers.registral.subasta.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class SubastaTransaction(actor: SubastaActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[SubastaExternalDto](monitoring) {
  def topic = "DGR-COP-SUBASTAS"
  def topicRetry = "DGR-COP-SUBASTAS_retry"
  def topicError = "DGR-COP-SUBASTAS_error"

  def processInput(input: String): Either[Throwable, SubastaExternalDto] =
    maybeDecode[SubastaExternalDto](input)

  override def processMessage(registro: SubastaExternalDto): Future[Response.SuccessProcessing] = {
    val command = SubastaCommands.SubastaUpdateFromDto(
      sujetoId = registro.BSB_SUJ_IDENTIFICADOR_ADQ,
      objetoId = registro.BSB_SOJ_IDENTIFICADOR,
      tipoObjeto = registro.BSB_SOJ_TIPO_OBJETO,
      subastaId = registro.BSB_SUB_ID,
      deliveryId = BigInt(registro.EV_ID),
      registro = registro
    )

    actor.ask(command)
  }

}
