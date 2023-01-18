package consumers.registral.plan_pago.application.entities

import java.time.LocalDateTime

sealed trait PlanPagoResponses
object PlanPagoResponses {

  case class GetPlanPagoResponse(registro: Option[PlanPagoExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
