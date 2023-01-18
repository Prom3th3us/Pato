package consumers.registral.etapas_procesales.domain.events

import java.time.LocalDateTime

import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import consumers.registral.etapas_procesales.domain.EtapasProcesalesState

class EtapasProcesalesUpdatedFromDtoHandler {
  def handle(state: EtapasProcesalesState, event: EtapasProcesalesUpdatedFromDto): EtapasProcesalesState =
    state
      .copy(
        registro = Some(event.registro),
        fechaUltMod = LocalDateTime.now
      )
}
