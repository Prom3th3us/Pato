package consumers.registral.parametrica_recargo.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoQueries.GetStateParametricaRecargo
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoResponses.GetParametricaRecargoResponse
import consumers.registral.parametrica_recargo.domain.{ParametricaRecargoEvents, ParametricaRecargoState}

class GetStateParametricaRecargoHandler() {
  def handle(
      query: GetStateParametricaRecargo
  )(state: ParametricaRecargoState)(replyTo: ActorRef[GetParametricaRecargoResponse]) =
    Effect.reply[
      GetParametricaRecargoResponse,
      ParametricaRecargoEvents,
      ParametricaRecargoState
    ](replyTo)(
      GetParametricaRecargoResponse(state.registro, state.fechaUltMod)
    )
}
