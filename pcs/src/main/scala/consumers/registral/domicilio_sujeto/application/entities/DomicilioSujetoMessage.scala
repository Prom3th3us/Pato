package consumers.registral.domicilio_sujeto.application.entities

import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoMessage.DomicilioSujetoMessageRoots

trait DomicilioSujetoMessage extends design_principles.actor_model.ShardedMessage with SujetoMessage {
  def domicilioId: String

  override def aggregateRoot: String =
    DomicilioSujetoMessageRoots(
      sujetoId,
      domicilioId
    ).toString
}

object DomicilioSujetoMessage {

  case class DomicilioSujetoMessageRoots(sujetoId: String, domicilioId: String) {
    override def toString = s"Sujeto-$sujetoId-DomicilioSujeto-$domicilioId"
  }
  object DomicilioSujetoMessageRoots {

    def extractor(persistenceId: String): DomicilioSujetoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-DomicilioSujeto-$domicilioId" =>
          DomicilioSujetoMessageRoots(sujetoId, domicilioId)
      }
  }
}
