package consumers.no_registral.sujeto.application.entity

import consumers.no_registral.sujeto.application.entity.SujetoMessage.SujetoMessageRoots
import design_principles.actor_model.ShardedMessage

trait SujetoMessage extends ShardedMessage {
  def sujetoId: String

  override def entityId: String = sujetoId
  override def shardedId: String = sujetoId

  override def aggregateRoot: String =
    SujetoMessageRoots(
      sujetoId
    ).toString
}
object SujetoMessage {

  case class SujetoMessageRoots(sujetoId: String) {
    override def toString: String = s"Sujeto-$sujetoId"
  }

  object SujetoMessageRoots {
    def extractor(persistenceId: String): SujetoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId" =>
          SujetoMessageRoots(sujetoId)
        case s"$sujetoId" =>
          SujetoMessageRoots(sujetoId)
      }
  }
}
