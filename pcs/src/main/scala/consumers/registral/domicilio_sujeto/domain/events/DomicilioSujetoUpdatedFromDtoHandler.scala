package consumers.registral.domicilio_sujeto.domain.events

import java.time.LocalDateTime

import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoState

class DomicilioSujetoUpdatedFromDtoHandler {
  def handle(state: DomicilioSujetoState, event: DomicilioSujetoUpdatedFromDto): DomicilioSujetoState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
