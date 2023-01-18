package consumers.registral.calendario.infrastructure

import consumers.registral.calendario.application.entities.CalendarioExternalDto
import consumers.registral.calendario.application.entities.CalendarioResponses.GetCalendarioResponse
import consumers.registral.calendario.domain.CalendarioEvents.CalendarioUpdatedFromDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val CalendarioF =
    Json.format[CalendarioExternalDto]

  implicit val CalendarioUpdateFromDtoF =
    Json.format[consumers.registral.calendario.application.entities.CalendarioCommands.CalendarioUpdateFromDto]
  implicit val CalendarioUpdatedFromDtoF =
    Json.format[consumers.registral.calendario.domain.CalendarioEvents.CalendarioUpdatedFromDto]
  class CalendarioUpdatedFromDtoFS extends EventSerializer[CalendarioUpdatedFromDto]

  implicit val GetCalendarioResponseF = Json.format[GetCalendarioResponse]
}
