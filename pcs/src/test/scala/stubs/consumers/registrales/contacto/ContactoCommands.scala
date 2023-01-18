package stubs.consumers.registrales.contacto

import consumers.registral.contacto.domain.ContactoCommands.ContactoUpdateFromDto
import stubs.consumers.registrales.contacto.ContactoExternalDto.contactoStub
import utils.generators.Model.deliveryId

object ContactoCommands {

  def contactoUpdateFromDtostub =
    ContactoUpdateFromDto(
      deliveryId = deliveryId,
      aggregateRoot = contactoStub.CNC_ID,
      registro = contactoStub
    )

}
