package consumers.registral.subasta.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.subasta.application.entities.SubastaQueries.GetStateSubasta
import consumers.registral.subasta.application.entities.SubastaResponses.GetSubastaResponse
import consumers.registral.subasta.domain.{SubastaEvents, SubastaState}

class GetStateSubastaHandler() {
  def handle(
      query: GetStateSubasta
  )(state: SubastaState)(replyTo: ActorRef[GetSubastaResponse]) =
    Effect.reply[
      GetSubastaResponse,
      SubastaEvents,
      SubastaState
    ](replyTo)(
      GetSubastaResponse(state.registro, state.fechaUltMod)
    )
}
