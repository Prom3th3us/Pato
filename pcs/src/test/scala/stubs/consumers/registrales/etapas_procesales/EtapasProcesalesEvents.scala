package stubs.consumers.registrales.etapas_procesales

import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import stubs.consumers.registrales.etapas_procesales.EtapasProcesalesExternalDto.{
  etapasProcesalesAntStub,
  etapasProcesalesTriStub
}
import stubs.consumers.registrales.juicio.JuicioExternalDto.juicioTriStub

object EtapasProcesalesEvents {
  def etapasProcesalesUpdatedFromDtoAntStub = EtapasProcesalesUpdatedFromDto(
    deliveryId = etapasProcesalesAntStub.EV_ID.toInt,
    juicioId = etapasProcesalesAntStub.BEP_JUI_ID,
    etapaId = etapasProcesalesAntStub.BPE_ETA_ID,
    registro = etapasProcesalesAntStub
  )
  def etapasProcesalesUpdatedFromDtoTriStub = EtapasProcesalesUpdatedFromDto(
    deliveryId = etapasProcesalesTriStub.EV_ID.toInt,
    juicioId = etapasProcesalesTriStub.BEP_JUI_ID,
    etapaId = etapasProcesalesTriStub.BPE_ETA_ID,
    registro = etapasProcesalesTriStub
  )
}
