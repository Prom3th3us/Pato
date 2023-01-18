package consumers.registral.juicio.infrastructure

import consumers.registral.juicio.application.entities.JuicioCommands.JuicioUpdateFromDto
import consumers.registral.juicio.application.entities.JuicioExternalDto
import consumers.registral.juicio.application.entities.JuicioExternalDto.{DetallesJuicio, JuicioAnt, JuicioTri}
import consumers.registral.juicio.application.entities.JuicioResponses.GetJuicioResponse
import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {

  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val JuicioAntF = Json.format[JuicioAnt]
  implicit val JuicioTriF = Json.format[JuicioTri]

  import io.leonard.TraitFormat
  import io.leonard.TraitFormat.traitFormat
  implicit val JuicioExternalDtoF: TraitFormat[JuicioExternalDto] =
    (traitFormat[JuicioExternalDto]
    << JuicioAntF
    << JuicioTriF)

  implicit val DetallesJuicioF = Json.format[DetallesJuicio]
  implicit val JuicioUpdateFromDtoF = Json.format[JuicioUpdateFromDto]
  implicit val JuicioUpdatedFromDtoF = Json.format[JuicioUpdatedFromDto]
  class JuicioUpdatedFromDtoFS extends EventSerializer[JuicioUpdatedFromDto]

  implicit val GetJuicioResponseF = Json.format[GetJuicioResponse]

}
