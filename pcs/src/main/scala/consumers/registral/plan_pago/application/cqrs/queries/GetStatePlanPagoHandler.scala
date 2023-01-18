package consumers.registral.plan_pago.application.cqrs.queries

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.plan_pago.application.entities.PlanPagoQueries.GetStatePlanPago
import consumers.registral.plan_pago.application.entities.PlanPagoResponses.GetPlanPagoResponse
import consumers.registral.plan_pago.domain.{PlanPagoEvents, PlanPagoState}

class GetStatePlanPagoHandler() {
  def handle(
      query: GetStatePlanPago
  )(state: PlanPagoState)(replyTo: ActorRef[GetPlanPagoResponse]) =
    Effect.reply[
      GetPlanPagoResponse,
      PlanPagoEvents,
      PlanPagoState
    ](replyTo)(
      GetPlanPagoResponse(state.registro, state.fechaUltMod)
    )
}
