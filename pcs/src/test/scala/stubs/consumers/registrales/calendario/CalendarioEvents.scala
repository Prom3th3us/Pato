package stubs.consumers.registrales.calendario

import consumers.registral.calendario.domain.CalendarioEvents.CalendarioUpdatedFromDto
import stubs.consumers.registrales.calendario.CalendarioExternalDtoStub.calendarioStub
import stubs.consumers.registrales.contacto.ContactoExternalDto.contactoStub

object CalendarioEvents {
  def calendarioUpdatedFromDtoStub = CalendarioUpdatedFromDto(
    deliveryId = calendarioStub.EV_ID.toInt,
    aggregateRoot = calendarioStub.BCL_IDENTIFICADOR,
    registro = calendarioStub
  )
}
