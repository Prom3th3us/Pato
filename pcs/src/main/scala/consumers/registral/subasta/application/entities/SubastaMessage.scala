package consumers.registral.subasta.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.subasta.application.entities.SubastaMessage.SubastaMessageRoots

trait SubastaMessage extends design_principles.actor_model.ShardedMessage with SujetoMessage with ObjetoMessage {
  def subastaId: String

  override def aggregateRoot: String =
    SubastaMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      subastaId
    ).toString

}

object SubastaMessage {

  case class SubastaMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String, subastaId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Subasta-$subastaId"
  }
  object SubastaMessageRoots {

    def extractor(persistenceId: String): SubastaMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Subasta-$subastaId" =>
          SubastaMessageRoots(sujetoId, objetoId, tipoObjeto, subastaId)
      }
  }
}
