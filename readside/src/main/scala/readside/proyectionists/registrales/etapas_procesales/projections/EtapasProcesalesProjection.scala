package readside.proyectionists.registrales.etapas_procesales.projections

import cassandra.CassandraTypesAdapter.int
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents
import cassandra.mechanism.UpdateReadSideProjection

trait EtapasProcesalesProjection extends UpdateReadSideProjection[EtapasProcesalesEvents] {
  def collectionName: String = "read_side.buc_etapas_procesales"

  val keys: List[(String, Object)] = List(
    "bep_jui_id" -> int(BigInt(event.juicioId)),
    "bpe_eta_id" -> event.etapaId
  )
}
