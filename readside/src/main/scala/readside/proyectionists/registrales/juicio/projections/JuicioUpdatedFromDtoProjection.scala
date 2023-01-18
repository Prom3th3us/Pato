package readside.proyectionists.registrales.juicio.projections
import consumers.registral.juicio.application.entities.JuicioExternalDto
import consumers.registral.juicio.domain.JuicioEvents

case class JuicioUpdatedFromDtoProjection(
    event: JuicioEvents.JuicioUpdatedFromDto
) extends JuicioProjection {
  val registro: JuicioExternalDto = event.registro

  def bindings: List[(String, Serializable)] = List(
    "bju_nro_externo" -> registro.BJU_NRO_EXTERNO,
    "bju_capital" -> registro.BJU_CAPITAL,
    "bju_estado" -> registro.BJU_ESTADO,
    "bju_fiscalizada" -> registro.BJU_FISCALIZADA,
    "bju_gastos" -> registro.BJU_GASTOS,
    "bju_gastos_mart" -> registro.BJU_GASTOS_MART,
    "bju_honorarios" -> registro.BJU_HONORARIOS,
    "bju_honorarios_mart" -> registro.BJU_HONORARIOS_MART,
    "bju_inicio_demanda" -> registro.BJU_INICIO_DEMANDA,
    "bju_interes_punit" -> registro.BJU_INTERES_PUNIT,
    "bju_interes_resar" -> registro.BJU_INTERES_RESAR,
    "bju_otros_atributos" -> registro.BJU_OTROS_ATRIBUTOS,
    "bju_pcr_id" -> registro.BJU_PCR_ID,
    "bju_porcentaje_iva" -> registro.BJU_PORCENTAJE_IVA,
    "bju_procurador" -> registro.BJU_PROCURADOR,
    "bju_tipo" -> registro.BJU_TIPO,
    "bju_total" -> registro.BJU_TOTAL
  )
}
