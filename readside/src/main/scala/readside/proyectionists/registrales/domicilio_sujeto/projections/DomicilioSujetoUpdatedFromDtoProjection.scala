package readside.proyectionists.registrales.domicilio_sujeto.projections
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoEvents

case class DomicilioSujetoUpdatedFromDtoProjection(
    event: DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto
) extends DomicilioSujetoProjection {
  val registro: DomicilioSujetoExternalDto = event.registro

  def bindings: List[(String, Option[String])] = List(
    "bds_barrio" -> registro.BDS_BARRIO,
    "bds_calle" -> registro.BDS_CALLE,
    "bds_codigo_postal" -> registro.BDS_CODIGO_POSTAL,
    "bds_dpto" -> registro.BDS_DPTO,
    "bds_estado" -> registro.BDS_ESTADO,
    "bds_kilometro" -> registro.BDS_KILOMETRO,
    "bds_localidad" -> registro.BDS_LOCALIDAD,
    "bds_lote" -> registro.BDS_LOTE,
    "bds_manzana" -> registro.BDS_MANZANA,
    "bds_piso" -> registro.BDS_PISO,
    "bds_provincia" -> registro.BDS_PROVINCIA,
    "bds_puerta" -> registro.BDS_PUERTA,
    "bds_tipo" -> registro.BDS_TIPO,
    "bds_torre" -> registro.BDS_TORRE,
    "bds_observaciones" -> registro.BDS_OBSERVACIONES
  )
}
