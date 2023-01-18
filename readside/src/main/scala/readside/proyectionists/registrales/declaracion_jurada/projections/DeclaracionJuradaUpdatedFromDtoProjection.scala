package readside.proyectionists.registrales.declaracion_jurada.projections

import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaExternalDto
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents

case class DeclaracionJuradaUpdatedFromDtoProjection(
    event: DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto
) extends DeclaracionJuradaProjection {
  val registro: DeclaracionJuradaExternalDto = event.registro
  def bindings: List[(String, Serializable)] = List(
    "bdj_cuota" -> registro.BDJ_CUOTA,
    "bdj_estado" -> registro.BDJ_ESTADO,
    "bdj_fiscalizada" -> registro.BDJ_FISCALIZADA,
    "bdj_impuesto_determinado" -> registro.BDJ_IMPUESTO_DETERMINADO,
    "bdj_obn_id" -> registro.BDJ_OBN_ID,
    "bdj_otros_atributos" -> registro.BDJ_OTROS_ATRIBUTOS,
    "bdj_percepciones" -> registro.BDJ_PERCEPCIONES,
    "bdj_periodo" -> registro.BDJ_PERIODO,
    "bdj_prorroga" -> registro.BDJ_PRORROGA,
    "bdj_recaudaciones" -> registro.BDJ_RECAUDACIONES,
    "bdj_retenciones" -> registro.BDJ_RETENCIONES,
    "bdj_tipo" -> registro.BDJ_TIPO,
    "bdj_total" -> registro.BDJ_TOTAL,
    "bdj_vencimiento" -> registro.BDJ_VENCIMIENTO
  )
}
