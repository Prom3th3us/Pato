package stubs.consumers.registrales.domicilio_sujeto

import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto
import stubs.consumers.registrales.domicilio_sujeto.DomicilioSujetoExternalDto.{
  domicilioSujetoAntStub,
  domicilioSujetoTriStub
}
import stubs.consumers.registrales.etapas_procesales.EtapasProcesalesExternalDto.etapasProcesalesTriStub

object DomicilioSujetoEvents {

  def domicilioSujetoUpdatedFromDtoAntStub: DomicilioSujetoUpdatedFromDto = DomicilioSujetoUpdatedFromDto(
    deliveryId = domicilioSujetoAntStub.EV_ID.toInt,
    sujetoId = domicilioSujetoAntStub.BDS_SUJ_IDENTIFICADOR,
    domicilioId = domicilioSujetoAntStub.BDS_DOM_ID,
    registro = domicilioSujetoAntStub
  )
  def domicilioSujetoUpdatedFromDtoTriStub: DomicilioSujetoUpdatedFromDto = DomicilioSujetoUpdatedFromDto(
    deliveryId = domicilioSujetoTriStub.EV_ID.toInt,
    sujetoId = domicilioSujetoTriStub.BDS_SUJ_IDENTIFICADOR,
    domicilioId = domicilioSujetoTriStub.BDS_DOM_ID,
    registro = domicilioSujetoTriStub
  )
}
