package consumers.registral.actividad_sujeto.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoCommands.ActividadSujetoUpdateFromDto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoResponses.GetActividadSujetoResponse
import consumers.registral.actividad_sujeto.domain.{ActividadSujetoEvents, ActividadSujetoState}
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val ActividadesSujetoF =
    Jsonx.formatCaseClass[
      consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto.ActividadSujeto
    ]

  implicit val ActividadSujetoUpdateFromDtoF = Json.format[ActividadSujetoUpdateFromDto]
  implicit val actividadSujetoDto: TraitFormat[ActividadSujetoExternalDto] =
    (traitFormat[ActividadSujetoExternalDto]
    << ActividadesSujetoF)
  implicit val ActividadSujetoStateF =
    Jsonx.formatCaseClass[ActividadSujetoState]
  implicit val ActividadSujetoUpdatedF = Jsonx.formatCaseClass[ActividadSujetoEvents.ActividadSujetoUpdatedFromDto]
  class ActividadSujetoUpdatedFromDtoFS extends EventSerializer[ActividadSujetoEvents.ActividadSujetoUpdatedFromDto]

  implicit val GetActividadSujetoResponseF = Json.format[GetActividadSujetoResponse]

}
