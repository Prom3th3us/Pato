package stubs.consumers.registrales.juicio

import consumers.registral.juicio.application.entities.JuicioCommands.JuicioUpdateFromDto
import consumers.registral.juicio.application.entities.JuicioExternalDto.DetallesJuicio
import consumers.registral.juicio.infrastructure.json._
import play.api.libs.json.Reads
import stubs.consumers.registrales.juicio.JuicioExternalDto.juicioTriStub
import utils.generators.Model.deliveryId

object JuicioCommands {

  private implicit val b: Reads[Seq[DetallesJuicio]] = Reads.seq(DetallesJuicioF.reads)

  val detallesJuicio: Option[Seq[DetallesJuicio]] = for {
    bjuDetalles <- (juicioTriStub.BJU_OTROS_ATRIBUTOS \ "BJU_DETALLES").toOption
    detalles = serialization.decodeF[Seq[DetallesJuicio]](bjuDetalles.toString)
  } yield detalles

  val juicioUpdateFromDtoTriStub = JuicioUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = juicioTriStub.BJU_SUJ_IDENTIFICADOR,
    objetoId = juicioTriStub.BJU_SOJ_IDENTIFICADOR,
    tipoObjeto = juicioTriStub.BJU_SOJ_TIPO_OBJETO,
    juicioId = juicioTriStub.BJU_JUI_ID,
    registro = juicioTriStub,
    detallesJuicio = detallesJuicio.getOrElse(Seq.empty)
  )
}
