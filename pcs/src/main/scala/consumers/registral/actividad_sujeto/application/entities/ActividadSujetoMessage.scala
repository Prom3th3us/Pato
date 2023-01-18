package consumers.registral.actividad_sujeto.application.entities

import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoMessage.ActividadSujetoMessageRoots
import design_principles.actor_model.ShardedMessage

trait ActividadSujetoMessage extends ShardedMessage with SujetoMessage {
  def actividadSujetoId: String

  override def aggregateRoot: String =
    ActividadSujetoMessageRoots(
      sujetoId,
      actividadSujetoId
    ).toString

}

object ActividadSujetoMessage {

  case class ActividadSujetoMessageRoots(sujetoId: String, actividadSujetoId: String) {
    override def toString = s"Sujeto-$sujetoId-ActividadSujeto-$actividadSujetoId"
  }
  object ActividadSujetoMessageRoots {

    def extractor(persistenceId: String): ActividadSujetoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-ActividadSujeto-$actividadSujetoId" =>
          ActividadSujetoMessageRoots(sujetoId, actividadSujetoId)
      }
  }
}
