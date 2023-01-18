package consumers.registral.tramite.application.entities

import java.time.LocalDateTime

import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import design_principles.actor_model.Response

sealed trait TramiteResponses
object TramiteResponses {

  case class GetTramiteResponse(registro: Option[Tramite] = None, fechaUltMod: LocalDateTime) extends Response
}
