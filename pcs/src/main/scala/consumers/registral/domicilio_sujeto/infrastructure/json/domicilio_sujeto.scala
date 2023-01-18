package consumers.registral.domicilio_sujeto.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoResponses.GetDomicilioSujetoResponse
import consumers.registral.domicilio_sujeto.domain.{DomicilioSujetoEvents, DomicilioSujetoState}
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val DomicilioSujetoTriF =
    Jsonx.formatCaseClass[
      consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.DomicilioSujetoTri
    ]
  implicit val DomicilioSujetoAntF =
    Jsonx.formatCaseClass[
      consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.DomicilioSujetoAnt
    ]

  implicit val domicilioSujetoDto
      : TraitFormat[consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto] =
    (traitFormat[consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto]
    << DomicilioSujetoTriF << DomicilioSujetoAntF)

  implicit val DomicilioSujetoStateF =
    Jsonx.formatCaseClass[DomicilioSujetoState]

  implicit val GetDomicilioSujetoResponseF = Json.format[GetDomicilioSujetoResponse]

  // DomicilioSujeto
  implicit val DomiciliSujetoUpdatedF = Jsonx.formatCaseClass[DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto]
  class DomicilioSujetoUpdatedFromDtoFS extends EventSerializer[DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto]

}
