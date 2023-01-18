package consumers.registral.tramite.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.tramite.application.entities.TramiteQueries.GetStateTramite
import consumers.registral.tramite.application.entities.TramiteResponses.GetTramiteResponse
import consumers.registral.tramite.domain.{TramiteEvents, TramiteState}

class GetStateTramiteHandler() {
  def handle(
      query: GetStateTramite
  )(state: TramiteState)(replyTo: ActorRef[GetTramiteResponse]) =
    Effect.reply[
      GetTramiteResponse,
      TramiteEvents,
      TramiteState
    ](replyTo)(
      GetTramiteResponse(state.registro, state.fechaUltMod)
    )
}
