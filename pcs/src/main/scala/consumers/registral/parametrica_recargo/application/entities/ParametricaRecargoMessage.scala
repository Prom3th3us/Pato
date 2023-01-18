package consumers.registral.parametrica_recargo.application.entities

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoMessage.ParametricaRecargoMessageRoots

trait ParametricaRecargoMessage extends design_principles.actor_model.ShardedMessage {
  val parametricaRecargoId: String

  override def aggregateRoot: String =
    ParametricaRecargoMessageRoots(
      parametricaRecargoId
    ).toString
}

object ParametricaRecargoMessage {

  case class ParametricaRecargoMessageRoots(parametricaRecargoId: String) {
    override def toString = s"ParametricaRecargo-$parametricaRecargoId"
  }
  object ParametricaRecargoMessageRoots {

    def extractor(persistenceId: String): ParametricaRecargoMessageRoots =
      persistenceId match {
        case s"ParametricaRecargo-$parametricaRecargoId" =>
          ParametricaRecargoMessageRoots(parametricaRecargoId)
      }
  }
}
