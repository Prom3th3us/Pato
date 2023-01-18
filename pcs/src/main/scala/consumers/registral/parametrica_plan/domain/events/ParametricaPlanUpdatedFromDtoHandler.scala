package consumers.registral.parametrica_plan.domain.events

import java.time.LocalDateTime

import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents.ParametricaPlanUpdatedFromDto
import consumers.registral.parametrica_plan.domain.ParametricaPlanState

class ParametricaPlanUpdatedFromDtoHandler {
  def handle(state: ParametricaPlanState, event: ParametricaPlanUpdatedFromDto): ParametricaPlanState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
