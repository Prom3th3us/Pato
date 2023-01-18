package readside.proyectionists.registrales.domicilio_objeto.projections
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents

case class DomicilioObjetoUpdatedFromDtoProjection(
    event: DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto
) extends DomicilioObjetoProjection {
  val registro: DomicilioObjetoExternalDto = event.registro
  def bindings: List[(String, Option[String])] = List(
    "bdo_barrio" -> registro.BDO_BARRIO,
    "bdo_calle" -> registro.BDO_CALLE,
    "bdo_codigo_postal" -> registro.BDO_CODIGO_POSTAL,
    "bdo_dpto" -> registro.BDO_DPTO,
    "bdo_estado" -> registro.BDO_ESTADO,
    "bdo_kilometro" -> registro.BDO_KILOMETRO,
    "bdo_localidad" -> registro.BDO_LOCALIDAD,
    "bdo_lote" -> registro.BDO_LOTE,
    "bdo_manzana" -> registro.BDO_MANZANA,
    "bdo_piso" -> registro.BDO_PISO,
    "bdo_provincia" -> registro.BDO_PROVINCIA,
    "bdo_puerta" -> registro.BDO_PUERTA,
    "bdo_tipo" -> registro.BDO_TIPO,
    "bdo_torre" -> registro.BDO_TORRE,
    "bdo_observaciones" -> registro.BDO_OBSERVACIONES
  )
}
