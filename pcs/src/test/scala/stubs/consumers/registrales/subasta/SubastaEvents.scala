package stubs.consumers.registrales.subasta

import consumers.registral.subasta.domain.SubastaEvents.SubastaUpdatedFromDto
import stubs.consumers.registrales.subasta.SubastaExternalDtoStub.subastaStub
import stubs.consumers.registrales.tramite.TramiteExternalDto.tramiteStub

object SubastaEvents {
  def subastaUpdatedFromDtoStub = SubastaUpdatedFromDto(
    deliveryId = subastaStub.EV_ID.toInt,
    sujetoId = subastaStub.BSB_SUJ_IDENTIFICADOR_ADQ,
    objetoId = subastaStub.BSB_SOJ_IDENTIFICADOR,
    tipoObjeto = subastaStub.BSB_SOJ_TIPO_OBJETO,
    subastaId = subastaStub.BSB_SUB_ID,
    registro = subastaStub
  )
}
