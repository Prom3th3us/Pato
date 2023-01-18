package consumers.registral.actividad_sujeto.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoQueries.GetStateActividadSujeto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoResponses.GetActividadSujetoResponse
import consumers.registral.actividad_sujeto.domain.{ActividadSujetoEvents, ActividadSujetoState}

class GetStateActividadSujetoHandler() {
  def handle(
      query: GetStateActividadSujeto
  )(state: ActividadSujetoState)(replyTo: ActorRef[GetActividadSujetoResponse]) =
    Effect.reply[
      GetActividadSujetoResponse,
      ActividadSujetoEvents,
      ActividadSujetoState
    ](replyTo)(
      GetActividadSujetoResponse(state.registro, state.fechaUltMod)
    )
}
