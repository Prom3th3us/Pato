package consumers.registral.parametrica_plan.application.entities

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanResponses.GetParametricaPlanResponse
import design_principles.actor_model.Query

sealed trait ParametricaPlanQueries extends ParametricaPlanMessage with Query
object ParametricaPlanQueries {
  case class GetStateParametricaPlan(parametricaPlanId: String) extends ParametricaPlanQueries {
    override type ReturnType = GetParametricaPlanResponse

  }
}
