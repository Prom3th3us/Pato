package consumers.registral.domicilio_objeto.infrastructure

import ai.x.play.json.Jsonx
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoResponses.GetDomicilioObjetoResponse
import consumers.registral.domicilio_objeto.domain.{DomicilioObjetoEvents, DomicilioObjetoState}
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val DomicilioObjetoAntF =
    Jsonx.formatCaseClass[
      consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto.DomicilioObjetoAnt
    ]

  implicit val DomicilioObjetoTriF =
    Jsonx.formatCaseClass[
      consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto.DomicilioObjetoTri
    ]

  implicit val domicilioObjetoDto: TraitFormat[DomicilioObjetoExternalDto] =
    (traitFormat[DomicilioObjetoExternalDto]
    << DomicilioObjetoAntF << DomicilioObjetoTriF)

  implicit val DomicilioObjetoStateF =
    Jsonx.formatCaseClass[DomicilioObjetoState]

  implicit val DomiciliObjetoUpdatedF = Jsonx.formatCaseClass[DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto]
  class DomicilioObjetoUpdatedFromDtoFS extends EventSerializer[DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto]

  implicit val GetDomicilioObjetoResponseF = Json.format[GetDomicilioObjetoResponse]

}
