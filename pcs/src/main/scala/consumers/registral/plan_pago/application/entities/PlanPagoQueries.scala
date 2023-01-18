package consumers.registral.plan_pago.application.entities

import consumers.registral.plan_pago.application.entities.PlanPagoResponses.GetPlanPagoResponse
import design_principles.actor_model.Query

sealed trait PlanPagoQueries extends Query with PlanPagoMessage

object PlanPagoQueries {
  case class GetStatePlanPago(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      planPagoId: String
  ) extends PlanPagoQueries {
    override type ReturnType = GetPlanPagoResponse
  }
}
