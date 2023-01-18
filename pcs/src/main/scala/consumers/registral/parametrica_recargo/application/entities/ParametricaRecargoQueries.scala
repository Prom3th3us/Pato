package consumers.registral.parametrica_recargo.application.entities

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoResponses.GetParametricaRecargoResponse
import design_principles.actor_model.Query

sealed trait ParametricaRecargoQueries extends ParametricaRecargoMessage with Query

object ParametricaRecargoQueries {
  case class GetStateParametricaRecargo(parametricaRecargoId: String) extends ParametricaRecargoQueries {
    override type ReturnType = GetParametricaRecargoResponse
  }
}
