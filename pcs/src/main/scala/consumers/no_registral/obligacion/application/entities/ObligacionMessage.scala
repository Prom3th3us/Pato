package consumers.no_registral.obligacion.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.obligacion.application.entities.ObligacionMessage.ObligacionMessageRoots
import consumers.no_registral.sujeto.application.entity.SujetoMessage

trait ObligacionMessage extends design_principles.actor_model.ShardedMessage with SujetoMessage with ObjetoMessage {
  def obligacionId: String
  override def aggregateRoot: String =
    ObligacionMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      obligacionId
    ).toString
}

object ObligacionMessage {

  case class ObligacionMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String, obligacionId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Obligacion-$obligacionId"
  }
  object ObligacionMessageRoots {

    def extractor(persistenceId: String): ObligacionMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Obligacion-$obligacionId" =>
          ObligacionMessageRoots(sujetoId, objetoId, tipoObjeto, obligacionId)
      }
  }
}
