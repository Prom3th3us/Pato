package consumers.registral.domicilio_objeto.domain.events

import java.time.LocalDateTime

import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoState

class DomicilioObjetoUpdatedFromDtoHandler {
  def handle(state: DomicilioObjetoState, event: DomicilioObjetoUpdatedFromDto): DomicilioObjetoState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
