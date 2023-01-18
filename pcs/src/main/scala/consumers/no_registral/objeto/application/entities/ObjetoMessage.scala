package consumers.no_registral.objeto.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage.ObjetoMessageRoots
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import design_principles.actor_model.ShardedMessage

trait ObjetoMessage extends ShardedMessage with SujetoMessage {
  def objetoId: String
  def tipoObjeto: String

  override def aggregateRoot: String =
    ObjetoMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto
    ).toString

}

object ObjetoMessage {
  case class ObjetoMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String) {
    override def toString: String = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto"
  }
  object ObjetoMessageRoots {
    def extractor(persistenceId: String): ObjetoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto" =>
          ObjetoMessageRoots(sujetoId, objetoId, tipoObjeto)
      }
  }
  case class ExencionMessageRoot(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      exencionId: String
  ) extends ObjetoMessage {
    override def aggregateRoot: String = s"${super.aggregateRoot}-Exencion-$exencionId"
  }
}
