package consumers.registral.parametrica_recargo.application.entities

import java.time.LocalDateTime

sealed trait ParametricaRecargoResponses
object ParametricaRecargoResponses {

  case class GetParametricaRecargoResponse(registro: Option[ParametricaRecargoExternalDto] = None,
                                           fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
