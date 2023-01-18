package readside.proyectionists.registrales.parametrica_plan.projections

import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents
import cassandra.mechanism.UpdateReadSideProjection

trait ParametricaPlanProjection extends UpdateReadSideProjection[ParametricaPlanEvents] {
  def collectionName: String = "read_side.buc_param_plan"

  val keys: List[(String, Object)] = List(
    "bpp_fpm_id" -> event.bppFpmId,
    "bpp_rdl_id" -> event.bppRdlId,
    "bpp_cant_max_cuotas" -> event.bppCantMaxCuotas,
    "bpp_cant_min_cuotas" -> event.bppCantMinCuotas,
    "bpp_dias_vto_cuotas" -> event.bppDiasVtoCuotas,
    "bpp_fecha_desde_deuda" -> event.bppFechaDesdeDeuda,
    "bpp_fecha_fin" -> event.bppFechaFin,
    "bpp_fecha_hasta_deuda" -> event.bppFechaHastaDeuda,
    "bpp_fecha_inicio" -> event.bppFechaInicio,
    "bpp_fpm_descripcion" -> event.bppFpmDescripcion,
    "bpp_indice_int_financ" -> event.bppIndiceIntFinanc,
    "bpp_indice_int_punit" -> event.bppIndiceIntPunit,
    "bpp_indice_int_resar" -> event.bppIndiceIntResar,
    "bpp_monto_max_deuda" -> event.bppMontoMaxDeuda,
    "bpp_monto_min_anticipo" -> event.bppMontoMinAnticipo,
    "bpp_monto_min_cuota" -> event.bppMontoMinCuota,
    "bpp_monto_min_deuda" -> event.bppMontoMinDeuda,
    "bpp_porcentaje_anticipo" -> event.bppPorcentajeAnticipo
  )
}
