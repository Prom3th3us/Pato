package stubs.consumers.registrales.domicilio_sujeto

import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoCommands.DomicilioSujetoUpdateFromDto
import stubs.consumers.registrales.domicilio_sujeto.DomicilioSujetoExternalDto.{
  domicilioSujetoAntStub,
  domicilioSujetoTriStub
}
import utils.generators.Model.deliveryId

object DomicilioSujetoCommands {
  def domicilioSujetoUpdateFromDtoTriStub = DomicilioSujetoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = domicilioSujetoTriStub.BDS_SUJ_IDENTIFICADOR,
    domicilioId = domicilioSujetoTriStub.BDS_DOM_ID,
    registro = domicilioSujetoTriStub
  )
  def domicilioSujetoUpdateFromDtoAntStub = DomicilioSujetoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = domicilioSujetoAntStub.BDS_SUJ_IDENTIFICADOR,
    domicilioId = domicilioSujetoAntStub.BDS_DOM_ID,
    registro = domicilioSujetoAntStub
  )
}
