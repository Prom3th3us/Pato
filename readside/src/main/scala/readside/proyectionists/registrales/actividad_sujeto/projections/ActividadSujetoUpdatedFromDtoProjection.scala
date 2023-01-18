package readside.proyectionists.registrales.actividad_sujeto.projections

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto
import consumers.registral.actividad_sujeto.domain.ActividadSujetoEvents

case class ActividadSujetoUpdatedFromDtoProjection(
    event: ActividadSujetoEvents.ActividadSujetoUpdatedFromDto
) extends ActividadSujetoProjection {
  val registro: ActividadSujetoExternalDto = event.registro
  def bindings: List[(String, Serializable)] = List(
    "bat_descripcion" -> registro.BAT_DESCRIPCION,
    "bat_fecha_fin" -> registro.BAT_FECHA_FIN,
    "bat_fecha_inicio" -> registro.BAT_FECHA_INICIO,
    "bat_otros_atributos" -> registro.BAT_OTROS_ATRIBUTOS,
    "bat_referencia" -> registro.BAT_REFERENCIA,
    "bat_tipo" -> registro.BAT_TIPO
  )
}
