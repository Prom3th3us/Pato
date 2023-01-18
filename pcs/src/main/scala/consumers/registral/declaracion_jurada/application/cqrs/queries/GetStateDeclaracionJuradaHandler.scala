package consumers.registral.declaracion_jurada.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaQueries.GetStateDeclaracionJurada
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaResponses.GetDeclaracionJuradaResponse
import consumers.registral.declaracion_jurada.domain.{DeclaracionJuradaEvents, DeclaracionJuradaState}

class GetStateDeclaracionJuradaHandler() {
  def handle(
      query: GetStateDeclaracionJurada
  )(state: DeclaracionJuradaState)(replyTo: ActorRef[GetDeclaracionJuradaResponse]) =
    Effect.reply[
      GetDeclaracionJuradaResponse,
      DeclaracionJuradaEvents,
      DeclaracionJuradaState
    ](replyTo)(
      GetDeclaracionJuradaResponse(state.registro, state.fechaUltMod)
    )
}
