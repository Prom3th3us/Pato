package consumers.registral.plan_pago.domain.events

import java.time.LocalDateTime

import consumers.registral.plan_pago.domain.PlanPagoEvents.PlanPagoUpdatedFromDto
import consumers.registral.plan_pago.domain.PlanPagoState

class PlanPagoUpdatedFromDtoHandler {
  def handle(state: PlanPagoState, event: PlanPagoUpdatedFromDto): PlanPagoState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
