package stubs.consumers.registrales.calendario

import consumers.registral.calendario.application.entities.CalendarioCommands.CalendarioUpdateFromDto
import stubs.consumers.registrales.calendario.CalendarioExternalDtoStub.calendarioStub
import utils.generators.Model.deliveryId

object CalendarioCommands {
  def calendarioUpdateFromDtoStub: CalendarioUpdateFromDto = CalendarioUpdateFromDto(
    deliveryId = deliveryId,
    calendarioId = calendarioStub.BCL_IDENTIFICADOR,
    registro = calendarioStub
  )
}
