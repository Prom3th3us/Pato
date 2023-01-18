package readside.proyectionists.registrales.plan_pago.projections
import cassandra.CassandraTypesAdapter.int
import consumers.registral.plan_pago.domain.PlanPagoEvents
import cassandra.mechanism.UpdateReadSideProjection

trait PlanPagoProjection extends UpdateReadSideProjection[PlanPagoEvents] {
  def collectionName: String = "read_side.buc_planes_pago"
  val keys: List[(String, Object)] = List(
    "bpl_suj_identificador" -> event.sujetoId,
    "bpl_soj_identificador" -> event.objetoId,
    "bpl_soj_tipo_objeto" -> event.tipoObjeto,
    "bpl_pln_id" -> int(Some(BigInt(event.planPagoId)))
  )
}
