package consumers.registral.domicilio_sujeto.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoQueries.GetStateDomicilioSujeto
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoResponses.GetDomicilioSujetoResponse
import consumers.registral.domicilio_sujeto.domain.{DomicilioSujetoEvents, DomicilioSujetoState}

class GetStateDomicilioSujetoHandler() {
  def handle(
      query: GetStateDomicilioSujeto
  )(state: DomicilioSujetoState)(replyTo: ActorRef[GetDomicilioSujetoResponse]) =
    Effect.reply[
      GetDomicilioSujetoResponse,
      DomicilioSujetoEvents,
      DomicilioSujetoState
    ](replyTo)(
      GetDomicilioSujetoResponse(state.registro, state.fechaUltMod)
    )
}
