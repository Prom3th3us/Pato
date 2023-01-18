package consumers.registral.parametrica_plan.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanQueries.GetStateParametricaPlan
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanResponses.GetParametricaPlanResponse
import consumers.registral.parametrica_plan.domain.{ParametricaPlanEvents, ParametricaPlanState}

class GetStateParametricaPlanHandler() {
  def handle(
      query: GetStateParametricaPlan
  )(state: ParametricaPlanState)(replyTo: ActorRef[GetParametricaPlanResponse]) =
    Effect.reply[
      GetParametricaPlanResponse,
      ParametricaPlanEvents,
      ParametricaPlanState
    ](replyTo)(
      GetParametricaPlanResponse(state.registro, state.fechaUltMod)
    )
}
