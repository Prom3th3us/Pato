package consumers.registral.parametrica_recargo.infrastructure

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoResponses.GetParametricaRecargoResponse
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val ParametricaRecargoTriF =
    Json.format[
      consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.ParametricaRecargoTri
    ]

  implicit val ParametricaRecargoAntF =
    Json.format[
      consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.ParametricaRecargoAnt
    ]

  import io.leonard.TraitFormat
  import io.leonard.TraitFormat.traitFormat
  implicit val ParametricaRecargoExternalDtoF: TraitFormat[ParametricaRecargoExternalDto] =
    (traitFormat[ParametricaRecargoExternalDto]
    << ParametricaRecargoAntF
    << ParametricaRecargoTriF)

  implicit val ParametricaRecargoUpdateFromDtoF = Json.format[ParametricaRecargoUpdateFromDto]
  implicit val ParametricaRecargoUpdatedFromDtoF = Json.format[ParametricaRecargoUpdatedFromDto]
  class ParametricaRecargoUpdatedFromDtoFS extends EventSerializer[ParametricaRecargoUpdatedFromDto]

  implicit val GetParametricaRecargoResponseF = Json.format[GetParametricaRecargoResponse]
}
