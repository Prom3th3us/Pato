package consumers.registral.calendario.domain.events

import java.time.LocalDateTime

import consumers.registral.calendario.domain.CalendarioEvents.CalendarioUpdatedFromDto
import consumers.registral.calendario.domain.CalendarioState

class CalendarioUpdatedFromDtoHandler {
  def handle(state: CalendarioState, event: CalendarioUpdatedFromDto): CalendarioState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
