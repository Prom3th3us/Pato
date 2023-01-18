package consumers.registral.subasta.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.subasta.application.entities.SubastaResponses.GetSubastaResponse
import consumers.registral.subasta.domain.{SubastaEvents, SubastaState}
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val SubastaExternalDtoF =
    Jsonx.formatCaseClass[consumers.registral.subasta.application.entities.SubastaExternalDto]

  implicit val SubastaStateF =
    Jsonx.formatCaseClass[SubastaState]
  implicit val SubastaUpdatedF = Jsonx.formatCaseClass[SubastaEvents.SubastaUpdatedFromDto]
  class SubastaUpdatedFromDtoFS extends EventSerializer[SubastaEvents.SubastaUpdatedFromDto]

  implicit val GetSubastaResponseF = Json.format[GetSubastaResponse]

}
