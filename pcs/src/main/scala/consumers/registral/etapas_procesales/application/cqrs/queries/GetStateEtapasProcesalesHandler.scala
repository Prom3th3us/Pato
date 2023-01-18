package consumers.registral.etapas_procesales.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesQueries.GetStateEtapasProcesales
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesResponses.GetEtapasProcesalesResponse
import consumers.registral.etapas_procesales.domain.{EtapasProcesalesEvents, EtapasProcesalesState}

class GetStateEtapasProcesalesHandler() {
  def handle(
      query: GetStateEtapasProcesales
  )(state: EtapasProcesalesState)(replyTo: ActorRef[GetEtapasProcesalesResponse]) =
    Effect.reply[
      GetEtapasProcesalesResponse,
      EtapasProcesalesEvents,
      EtapasProcesalesState
    ](replyTo)(
      GetEtapasProcesalesResponse(state.registro, state.fechaUltMod)
    )
}
