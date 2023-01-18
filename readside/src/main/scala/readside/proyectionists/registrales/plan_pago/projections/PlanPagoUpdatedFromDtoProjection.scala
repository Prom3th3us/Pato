package readside.proyectionists.registrales.plan_pago.projections
import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto
import consumers.registral.plan_pago.domain.PlanPagoEvents

case class PlanPagoUpdatedFromDtoProjection(
    event: PlanPagoEvents.PlanPagoUpdatedFromDto
) extends PlanPagoProjection {
  val registro: PlanPagoExternalDto = event.registro

  def bindings: List[(String, Serializable)] = List(
    "bpl_cantidad_cuotas" -> registro.BPL_CANTIDAD_CUOTAS,
    "bpl_estado" -> registro.BPL_ESTADO,
    "bpl_fecha_act_deuda" -> registro.BPL_FECHA_ACT_DEUDA,
    "bpl_fecha_emision" -> registro.BPL_FECHA_EMISION,
    "bpl_importe_a_financiar" -> registro.BPL_IMPORTE_A_FINANCIAR,
    "bpl_importe_anticipo" -> registro.BPL_IMPORTE_ANTICIPO,
    "bpl_importe_financiado" -> registro.BPL_IMPORTE_ANTICIPO,
    "bpl_nro_referencia" -> registro.BPL_NRO_REFERENCIA,
    "bpl_tipo" -> registro.BPL_TIPO,
    "bpl_otros_atributos" -> registro.BPL_OTROS_ATRIBUTOS
  )
}
