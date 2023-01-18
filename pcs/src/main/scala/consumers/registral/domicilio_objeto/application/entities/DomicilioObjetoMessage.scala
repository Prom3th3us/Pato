package consumers.registral.domicilio_objeto.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoMessage.DomicilioObjetoMessageRoots

trait DomicilioObjetoMessage
    extends design_principles.actor_model.ShardedMessage
    with SujetoMessage
    with ObjetoMessage {
  def domicilioId: String

  override def aggregateRoot: String =
    DomicilioObjetoMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      domicilioId
    ).toString
}

object DomicilioObjetoMessage {

  case class DomicilioObjetoMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String, domicilioId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-DomicilioObjeto-$domicilioId"
  }
  object DomicilioObjetoMessageRoots {

    def extractor(persistenceId: String): DomicilioObjetoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-DomicilioObjeto-$domicilioId" =>
          DomicilioObjetoMessageRoots(sujetoId, objetoId, tipoObjeto, domicilioId)
      }
  }
}
