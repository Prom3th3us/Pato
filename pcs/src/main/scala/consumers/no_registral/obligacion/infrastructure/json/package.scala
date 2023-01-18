package consumers.no_registral.obligacion.infrastructure

import ai.x.play.json.Jsonx
import consumers.no_registral.obligacion.application.entities.ObligacionCommands.{
  ObligacionUpdateExencion,
  ObligacionUpdateFromDto
}
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{
  DetallesObligacion,
  ObligacionesAnt,
  ObligacionesTri
}
import consumers.no_registral.obligacion.application.entities.ObligacionResponses.GetObligacionResponse
import consumers.no_registral.obligacion.domain.ObligacionEvents.{
  ObligacionAddedExencion,
  ObligacionPersistedSnapshot,
  ObligacionRemoved,
  ObligacionUpdatedFromDto
}
import consumers.no_registral.obligacion.domain.ObligacionState
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  private implicit val ExencionFF = consumers.no_registral.objeto.infrastructure.json.ExencionF

  implicit val DetallesObligacionF = Json.format[DetallesObligacion]
  implicit val ObligacionUpdateFromDtoF = Jsonx.formatCaseClass[ObligacionUpdateFromDto]

  implicit val ObligacionesTriF = Jsonx.formatCaseClass[ObligacionesTri]
  implicit val ObligacionesAntF = Jsonx.formatCaseClass[ObligacionesAnt]
  implicit val ObligacionesDto: TraitFormat[ObligacionExternalDto] =
    (traitFormat[ObligacionExternalDto]
    << ObligacionesAntF
    << ObligacionesTriF)
  implicit val ObligacionUpdatedFromDtoF = Jsonx.formatCaseClass[ObligacionUpdatedFromDto]
  class ObligacionUpdatedFromDtoFS extends EventSerializer[ObligacionUpdatedFromDto]
  implicit val ObligacionRemovedF = Jsonx.formatCaseClass[ObligacionRemoved]
  class ObligacionRemovedFS extends EventSerializer[ObligacionRemoved]

  implicit val ObligacionStateF = Jsonx.formatCaseClass[ObligacionState]
  implicit val GetObligacionResponseF = Json.format[GetObligacionResponse]
  implicit val ObligacionUpdateExencionF = Json.format[ObligacionUpdateExencion]
  implicit val ObligacionAddedExencionF = Json.format[ObligacionAddedExencion]
  class ObligacionAddedExencionFS extends EventSerializer[ObligacionAddedExencion]

  implicit val ObligacionPersistedSnapshotF = Json.format[ObligacionPersistedSnapshot]
  class ObligacionPersistedSnapshotFS extends EventSerializer[ObligacionPersistedSnapshot]
}
