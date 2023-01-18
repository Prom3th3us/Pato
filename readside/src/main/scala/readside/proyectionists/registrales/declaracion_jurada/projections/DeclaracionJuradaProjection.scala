package readside.proyectionists.registrales.declaracion_jurada.projections

import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents
import cassandra.mechanism.UpdateReadSideProjection

trait DeclaracionJuradaProjection extends UpdateReadSideProjection[DeclaracionJuradaEvents] {
  def collectionName: String = "read_side.buc_declaraciones_juradas"

  val keys: List[(String, Object)] = List(
    "bdj_suj_identificador" -> event.sujetoId,
    "bdj_soj_tipo_objeto" -> event.tipoObjeto,
    "bdj_soj_identificador" -> event.objetoId,
    "bdj_ddj_id" -> event.declaracionJuradaId
  )
}
