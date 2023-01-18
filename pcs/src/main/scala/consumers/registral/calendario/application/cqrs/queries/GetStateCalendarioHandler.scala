package consumers.registral.calendario.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.calendario.application.entities.CalendarioQueries.GetStateCalendario
import consumers.registral.calendario.application.entities.CalendarioResponses.GetCalendarioResponse
import consumers.registral.calendario.domain.{CalendarioEvents, CalendarioState}

class GetStateCalendarioHandler() {
  def handle(
      query: GetStateCalendario
  )(state: CalendarioState)(replyTo: ActorRef[GetCalendarioResponse]) =
    Effect.reply[
      GetCalendarioResponse,
      CalendarioEvents,
      CalendarioState
    ](replyTo)(
      GetCalendarioResponse(state.registro, state.fechaUltMod)
    )
}
