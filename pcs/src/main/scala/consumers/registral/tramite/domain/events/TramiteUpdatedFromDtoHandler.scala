package consumers.registral.tramite.domain.events

import java.time.LocalDateTime

import consumers.registral.tramite.domain.TramiteEvents.TramiteUpdatedFromDto
import consumers.registral.tramite.domain.TramiteState

class TramiteUpdatedFromDtoHandler {
  def handle(state: TramiteState, event: TramiteUpdatedFromDto): TramiteState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
