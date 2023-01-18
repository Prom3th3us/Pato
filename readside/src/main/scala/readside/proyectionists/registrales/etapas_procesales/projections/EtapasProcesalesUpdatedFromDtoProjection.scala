package readside.proyectionists.registrales.etapas_procesales.projections
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents

case class EtapasProcesalesUpdatedFromDtoProjection(
    event: EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
) extends EtapasProcesalesProjection {
  val registro: EtapasProcesalesExternalDto = event.registro

  def bindings: List[(String, Serializable)] = List(
    "bep_descripcion" -> registro.BEP_DESCRIPCION,
    "bep_fecha_fin" -> registro.BEP_FECHA_FIN,
    "bep_fecha_inicio" -> registro.BEP_FECHA_INICIO,
    "bep_otros_atributos" -> registro.BEP_OTROS_ATRIBUTOS,
    "bep_referencia" -> registro.BEP_REFERENCIA,
    "bep_tipo" -> registro.BEP_TIPO
  )
}
