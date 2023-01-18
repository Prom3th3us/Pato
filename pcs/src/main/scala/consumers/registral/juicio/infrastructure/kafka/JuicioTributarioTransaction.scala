package consumers.registral.juicio.infrastructure.kafka

import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.registral.juicio.application.entities.JuicioCommands
import consumers.registral.juicio.application.entities.JuicioExternalDto.{DetallesJuicio, JuicioTri}
import consumers.registral.juicio.infrastructure.dependency_injection.JuicioActor
import consumers.registral.juicio.infrastructure.json._
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import monitoring.Monitoring
import play.api.libs.json.Reads
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class JuicioTributarioTransaction(actor: JuicioActor, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[JuicioTri](monitoring) {

  def topic = "DGR-COP-JUICIOS-TRI"
  def topicRetry = "DGR-COP-JUICIOS-TRI_retry"
  def topicError = "DGR-COP-JUICIOS-TRI_error"

  def processInput(input: String): Either[Throwable, JuicioTri] = {
    maybeDecode[JuicioTri](input)
  }

  override def processMessage(registro: JuicioTri): Future[Response.SuccessProcessing] = {
    implicit val b: Reads[Seq[DetallesJuicio]] = Reads.seq(DetallesJuicioF.reads)

    val detalles: Option[Seq[DetallesJuicio]] = for {
      bjuDetalles <- (registro.BJU_OTROS_ATRIBUTOS \ "BJU_DETALLES").toOption
      detalles = serialization.decodeF[Seq[DetallesJuicio]](bjuDetalles.toString)
    } yield detalles

    val command: JuicioCommands.JuicioUpdateFromDto =
      JuicioCommands.JuicioUpdateFromDto(
        sujetoId = registro.BJU_SUJ_IDENTIFICADOR,
        objetoId = registro.BJU_SOJ_IDENTIFICADOR,
        tipoObjeto = registro.BJU_SOJ_TIPO_OBJETO,
        juicioId = registro.BJU_JUI_ID,
        deliveryId = BigInt(registro.EV_ID),
        registro = registro,
        detalles.getOrElse(Seq.empty)
      )

    actor.ask(command)
  }

}
