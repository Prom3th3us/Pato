package consumers.registral.juicio.domain.events

import java.time.LocalDateTime

import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import consumers.registral.juicio.domain.JuicioState

class JuicioUpdatedFromDtoHandler {
  def handle(state: JuicioState, event: JuicioUpdatedFromDto): JuicioState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
