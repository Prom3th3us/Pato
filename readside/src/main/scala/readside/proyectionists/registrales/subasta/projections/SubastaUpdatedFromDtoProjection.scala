package readside.proyectionists.registrales.subasta.projections
import consumers.registral.subasta.application.entities.SubastaExternalDto
import consumers.registral.subasta.domain.SubastaEvents

case class SubastaUpdatedFromDtoProjection(
    event: SubastaEvents.SubastaUpdatedFromDto
) extends SubastaProjection {
  val registro: SubastaExternalDto = event.registro

  def bindings = List(
    "bsb_auto" -> registro.BSB_AUTO,
    "bsb_fecha_fin" -> registro.BSB_FECHA_FIN,
    "bsb_fecha_inicio" -> registro.BSB_FECHA_INICIO,
    "bsb_suj_identificador_sub" -> registro.BSB_SUJ_IDENTIFICADOR_SUB,
    "bsb_tipo" -> registro.BSB_TIPO
  )
}
