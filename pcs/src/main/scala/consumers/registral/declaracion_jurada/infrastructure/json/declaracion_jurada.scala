package consumers.registral.declaracion_jurada.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaExternalDto
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaResponses.GetDeclaracionJuradaResponse
import consumers.registral.declaracion_jurada.domain.{DeclaracionJuradaEvents, DeclaracionJuradaState}
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.{Json, OFormat}
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val DeclaracionJuradaF =
    Jsonx.formatCaseClass[
      consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaExternalDto.DeclaracionJurada
    ]

  implicit val declaracionJuradaDto: TraitFormat[DeclaracionJuradaExternalDto] =
    (traitFormat[DeclaracionJuradaExternalDto]
    << DeclaracionJuradaF)

  implicit val DeclaracionJuradaStateF: OFormat[DeclaracionJuradaState] =
    Jsonx.formatCaseClass[DeclaracionJuradaState]

  implicit val DeclaracionJuradaUpdatedF =
    Jsonx.formatCaseClass[DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto]
  class DeclaracionJuradaUpdatedFromDtoFS
      extends EventSerializer[DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto]

  implicit val GetDeclaracionJuradaResponseF = Json.format[GetDeclaracionJuradaResponse]

}
