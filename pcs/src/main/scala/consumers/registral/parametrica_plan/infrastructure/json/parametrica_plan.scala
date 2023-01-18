package consumers.registral.parametrica_plan.infrastructure

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanCommands.ParametricaPlanUpdateFromDto
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto.{
  ParametricaPlanAnt,
  ParametricaPlanTri
}
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanResponses.GetParametricaPlanResponse
import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents.ParametricaPlanUpdatedFromDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val ParametricaPlanTriF = Json.format[ParametricaPlanTri]
  implicit val ParametricaPlanAntF = Json.format[ParametricaPlanAnt]

  import io.leonard.TraitFormat
  import io.leonard.TraitFormat.traitFormat
  implicit val ParametricaPlanExternalDtoF: TraitFormat[ParametricaPlanExternalDto] =
    (traitFormat[ParametricaPlanExternalDto]
    << ParametricaPlanAntF
    << ParametricaPlanTriF)

  implicit val ParametricaPlanUpdateFromDtoF = Json.format[ParametricaPlanUpdateFromDto]
  implicit val ParametricaPlanUpdatedFromDtoF = Json.format[ParametricaPlanUpdatedFromDto]
  class ParametricaPlanUpdatedFromDtoFS extends EventSerializer[ParametricaPlanUpdatedFromDto]

  implicit val GetParametricaPlanResponseF = Json.format[GetParametricaPlanResponse]
}
