package readside.proyectionists.no_registrales.sujeto.projections

import consumers.no_registral.sujeto.domain.SujetoEvents
import cassandra.mechanism.UpdateReadSideProjection

trait SujetoProjection extends UpdateReadSideProjection[SujetoEvents] {
  def collectionName: String = "read_side.buc_sujeto"
  val keys: List[(String, Object)] = List(
    "suj_identificador" -> event.sujetoId
  )
}
