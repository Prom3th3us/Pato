package consumers.registral.juicio.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.juicio.application.entities.JuicioQueries.GetStateJuicio
import consumers.registral.juicio.application.entities.JuicioResponses.GetJuicioResponse
import consumers.registral.juicio.domain.{JuicioEvents, JuicioState}

class GetStateJuicioHandler() {
  def handle(
      query: GetStateJuicio
  )(state: JuicioState)(replyTo: ActorRef[GetJuicioResponse]) =
    Effect.reply[
      GetJuicioResponse,
      JuicioEvents,
      JuicioState
    ](replyTo)(
      GetJuicioResponse(state.registro, state.detallesJuicio, state.fechaUltMod)
    )
}
