package consumers.registral.domicilio_objeto.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoQueries.GetStateDomicilioObjeto
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoResponses.GetDomicilioObjetoResponse
import consumers.registral.domicilio_objeto.domain.{DomicilioObjetoEvents, DomicilioObjetoState}

class GetStateDomicilioObjetoHandler() {
  def handle(
      query: GetStateDomicilioObjeto
  )(state: DomicilioObjetoState)(replyTo: ActorRef[GetDomicilioObjetoResponse]) =
    Effect.reply[
      GetDomicilioObjetoResponse,
      DomicilioObjetoEvents,
      DomicilioObjetoState
    ](replyTo)(
      GetDomicilioObjetoResponse(state.registro, state.fechaUltMod)
    )
}
