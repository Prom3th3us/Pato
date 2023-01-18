package consumers.registral.parametrica_plan.application.entities

import java.time.LocalDateTime

sealed trait ParametricaPlanResponses
object ParametricaPlanResponses {

  case class GetParametricaPlanResponse(registro: Option[ParametricaPlanExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
