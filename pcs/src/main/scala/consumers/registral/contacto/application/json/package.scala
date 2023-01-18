package consumers.registral.contacto.application

import consumers.registral.contacto.domain.ContactoCommands.ContactoUpdateFromDto
import consumers.registral.contacto.domain.ContactoEvents.ContactoUpdatedFromDto
import consumers.registral.contacto.domain.ContactoExternalDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val ContactoExternalDtoF = Json.format[ContactoExternalDto]
  implicit val ContactoUpdateFromDtoF = Json.format[ContactoUpdateFromDto]
  implicit val ContactoUpdatedFromDtoF = Json.format[ContactoUpdatedFromDto]
  class ContactoUpdatedFromDtoFS extends EventSerializer[ContactoUpdatedFromDto]
}
