package stubs.consumers.registrales.etapas_procesales

import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesCommands.EtapasProcesalesUpdateFromDto
import stubs.consumers.registrales.etapas_procesales.EtapasProcesalesExternalDto.{
  etapasProcesalesAntStub,
  etapasProcesalesTriStub
}
import utils.generators.Model.deliveryId

object EtapasProcesalesCommands {
  def etapasProcesalesUpdateFromDtoAntStub: EtapasProcesalesUpdateFromDto = EtapasProcesalesUpdateFromDto(
    deliveryId = deliveryId,
    juicioId = etapasProcesalesAntStub.BEP_JUI_ID,
    etapaId = etapasProcesalesAntStub.BPE_ETA_ID,
    registro = etapasProcesalesAntStub
  )
  def etapasProcesalesUpdateFromDtoTriStub: EtapasProcesalesUpdateFromDto = EtapasProcesalesUpdateFromDto(
    deliveryId = deliveryId,
    juicioId = etapasProcesalesTriStub.BEP_JUI_ID,
    etapaId = etapasProcesalesTriStub.BPE_ETA_ID,
    registro = etapasProcesalesTriStub
  )
}
