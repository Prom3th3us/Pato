package stubs.consumers.registrales.subasta

import consumers.registral.subasta.application.entities.SubastaCommands.SubastaUpdateFromDto
import stubs.consumers.registrales.subasta.SubastaExternalDtoStub.subastaStub
import utils.generators.Model.deliveryId

object SubastaCommands {
  def subastaUpdateFromDtoStub: SubastaUpdateFromDto = SubastaUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = subastaStub.BSB_SUJ_IDENTIFICADOR_ADQ,
    objetoId = subastaStub.BSB_SOJ_IDENTIFICADOR,
    tipoObjeto = subastaStub.BSB_SOJ_TIPO_OBJETO,
    subastaId = subastaStub.BSB_SUB_ID,
    registro = subastaStub
  )
}
