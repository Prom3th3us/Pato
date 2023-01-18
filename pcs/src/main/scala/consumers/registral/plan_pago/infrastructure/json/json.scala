package consumers.registral.plan_pago.infrastructure

import java.time.LocalDateTime

import consumers.registral.plan_pago.application.entities.PlanPagoCommands.PlanPagoUpdateFromDto
import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto
import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto.{PlanPagoAnt, PlanPagoTri}
import consumers.registral.plan_pago.application.entities.PlanPagoResponses.GetPlanPagoResponse
import consumers.registral.plan_pago.domain.PlanPagoEvents.PlanPagoUpdatedFromDto
import play.api.libs.json.{Format, Json}
import serialization.EventSerializer
package object json {
  implicit val localdatetimeF: Format[LocalDateTime] = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val PlanPagoAntF = Json.format[PlanPagoAnt]
  implicit val PlanPagoTriF = Json.format[PlanPagoTri]

  import io.leonard.TraitFormat
  import io.leonard.TraitFormat.traitFormat
  implicit val PlanPagoExternalDtoF: TraitFormat[PlanPagoExternalDto] =
    (traitFormat[PlanPagoExternalDto]
    << PlanPagoAntF
    << PlanPagoTriF)

  implicit val PlanPagoUpdateFromDtoF = Json.format[PlanPagoUpdateFromDto]
  implicit val PlanPagoUpdatedFromDtoF = Json.format[PlanPagoUpdatedFromDto]
  class PlanPagoUpdatedFromDtoFS extends EventSerializer[PlanPagoUpdatedFromDto]

  implicit val GetPlanPagoResponseF = Json.format[GetPlanPagoResponse]
}
