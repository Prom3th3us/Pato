package readside.proyectionists.registrales.tramite.projections
import consumers.registral.tramite.application.entities.TramiteExternalDto
import consumers.registral.tramite.domain.TramiteEvents

case class TramiteUpdatedFromDtoProjection(
    event: TramiteEvents.TramiteUpdatedFromDto
) extends TramiteProjection {
  val registro: TramiteExternalDto.Tramite = event.registro

  def bindings: List[(String, IterableOnce[Serializable] with Equals)] = List(
    "btr_archivos" -> registro.BTR_ARCHIVOS,
    "btr_descripcion" -> registro.BTR_DESCRIPCION,
    "btr_estado" -> registro.BTR_ESTADO,
    "btr_fecha_inicio" -> registro.BTR_FECHA_INICIO,
    "btr_otros_atributos" -> registro.BTR_OTROS_ATRIBUTOS,
    "btr_referencia" -> registro.BTR_REFERENCIA,
    "btr_tipo" -> registro.BTR_TIPO
  )
}
