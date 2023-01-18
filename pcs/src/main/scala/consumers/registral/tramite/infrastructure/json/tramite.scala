package consumers.registral.tramite.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.tramite.application.entities.TramiteResponses.GetTramiteResponse
import consumers.registral.tramite.domain.{TramiteEvents, TramiteState}
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val TramiteF =
    Jsonx.formatCaseClass[consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite]
  implicit val TramiteStateF =
    Jsonx.formatCaseClass[TramiteState]
  implicit val TramiteUpdatedF = Jsonx.formatCaseClass[TramiteEvents.TramiteUpdatedFromDto]
  class TramiteUpdatedFromDtoFS extends EventSerializer[TramiteEvents.TramiteUpdatedFromDto]
  implicit val GetTramiteResponseF = Json.format[GetTramiteResponse]

}
