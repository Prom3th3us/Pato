package readside.proyectionists.registrales.domicilio_objeto.projections

import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents
import cassandra.mechanism.UpdateReadSideProjection

trait DomicilioObjetoProjection extends UpdateReadSideProjection[DomicilioObjetoEvents] {
  def collectionName: String = "read_side.buc_domicilios_objeto"

  val keys: List[(String, Object)] = List(
    "bdo_suj_identificador" -> event.sujetoId,
    "bdo_soj_identificador" -> event.objetoId,
    "bdo_soj_tipo_objeto" -> event.tipoObjeto,
    "bdo_dom_id" -> event.domicilioId
  )
}
