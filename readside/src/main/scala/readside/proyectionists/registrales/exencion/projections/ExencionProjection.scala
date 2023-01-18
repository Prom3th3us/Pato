package readside.proyectionists.registrales.exencion.projections

import consumers.no_registral.objeto.domain.ObjetoEvents
import cassandra.mechanism.UpdateReadSideProjection

trait ExencionProjection extends UpdateReadSideProjection[ObjetoEvents.ObjetoAddedExencion] {
  def collectionName: String = "read_side.buc_exenciones"

  val keys: List[(String, Object)] = List(
    "bex_suj_identificador" -> event.sujetoId,
    "bex_soj_tipo_objeto" -> event.tipoObjeto,
    "bex_soj_identificador" -> event.objetoId,
    "bex_exe_id" -> event.exencion.BEX_EXE_ID
  )
}
