package readside.proyectionists.registrales.parametrica_recargo.projections

import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents
import cassandra.mechanism.UpdateReadSideProjection

trait ParametricaRecargoProjection extends UpdateReadSideProjection[ParametricaRecargoEvents] {
  def collectionName: String = "read_side.buc_param_recargo"
  val keys: List[(String, Object)] = List(
    "bpr_indice" -> event.bprIndice,
    "bpr_tipo_indice" -> event.bprTipoIndice,
    "bpr_fecha_desde" -> event.bprFechaDesde,
    "bpr_periodo" -> event.bprPeriodo,
    "bpr_concepto" -> event.bprConcepto,
    "bpr_impuesto" -> event.bprImpuesto
  )
}
