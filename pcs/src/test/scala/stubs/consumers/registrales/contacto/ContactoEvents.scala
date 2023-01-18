package stubs.consumers.registrales.contacto

import consumers.registral.contacto.domain.ContactoEvents.ContactoUpdatedFromDto
import stubs.consumers.registrales.contacto.ContactoExternalDto.contactoStub
import stubs.consumers.registrales.declaracion_jurada.DeclaracionJuradaExternalDto.declaracionJuradaStub

object ContactoEvents {

  def contactoUpdateFromDtoStub =
    ContactoUpdatedFromDto(
      deliveryId = 0,
      aggregateRoot = contactoStub.CNC_ID,
      registro = contactoStub
    )

}
