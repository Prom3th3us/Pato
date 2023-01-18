package readside.proyectionists.no_registrales.obligacion.projectionists

import consumers.no_registral.obligacion.domain.ObligacionEvents
import cassandra.mechanism.UpdateReadSideProjection

trait ObligacionProjection extends UpdateReadSideProjection[ObligacionEvents] {
  def collectionName: String = "read_side.buc_obligaciones"
  val keys: List[(String, Object)] = List(
    "bob_suj_identificador" -> event.sujetoId,
    "bob_soj_tipo_objeto" -> event.tipoObjeto,
    "bob_soj_identificador" -> event.objetoId,
    "bob_obn_id" -> event.obligacionId
  )
}
