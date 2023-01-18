package consumers.registral.calendario.application.entities

import java.time.LocalDateTime

sealed trait CalendarioResponses
object CalendarioResponses {

  case class GetCalendarioResponse(registro: Option[CalendarioExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
