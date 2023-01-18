package consumers.registral.tramite.application.entities

import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.tramite.application.entities.TramiteMessage.TramiteMessageRoots

trait TramiteMessage extends design_principles.actor_model.ShardedMessage with SujetoMessage {
  def tramiteId: String

  override def aggregateRoot: String =
    TramiteMessageRoots(
      sujetoId,
      tramiteId
    ).toString

}

object TramiteMessage {

  case class TramiteMessageRoots(sujetoId: String, tramiteId: String) {
    override def toString = s"Sujeto-$sujetoId-Tramite-$tramiteId"
  }
  object TramiteMessageRoots {

    def extractor(persistenceId: String): TramiteMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Tramite-$tramiteId" =>
          TramiteMessageRoots(sujetoId, tramiteId)
      }
  }
}
