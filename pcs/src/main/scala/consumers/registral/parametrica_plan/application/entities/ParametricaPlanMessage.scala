package consumers.registral.parametrica_plan.application.entities

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanMessage.ParametricaPlanMessageRoots

trait ParametricaPlanMessage extends design_principles.actor_model.ShardedMessage {

  val parametricaPlanId: String

  override def aggregateRoot: String =
    ParametricaPlanMessageRoots(
      parametricaPlanId
    ).toString
}

object ParametricaPlanMessage {

  case class ParametricaPlanMessageRoots(parametricaPlanId: String) {
    override def toString = s"ParametricaPlan-$parametricaPlanId"
  }
  object ParametricaPlanMessageRoots {

    def extractor(persistenceId: String): ParametricaPlanMessageRoots =
      persistenceId match {
        case s"ParametricaPlan-$parametricaPlanId" =>
          ParametricaPlanMessageRoots(parametricaPlanId)
      }
  }
}
