package stubs.consumers.registrales.declaracion_jurada

import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaCommands.DeclaracionJuradaUpdateFromDto
import stubs.consumers.registrales.declaracion_jurada.DeclaracionJuradaExternalDto.declaracionJuradaStub
import utils.generators.Model.deliveryId

object DeclaracionJuradaCommands {
  def declaracionJuradaUpdateFromDtoStub: DeclaracionJuradaUpdateFromDto = DeclaracionJuradaUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = declaracionJuradaStub.BDJ_SUJ_IDENTIFICADOR,
    objetoId = declaracionJuradaStub.BDJ_SOJ_IDENTIFICADOR,
    tipoObjeto = declaracionJuradaStub.BDJ_SOJ_TIPO_OBJETO,
    declaracionJuradaId = declaracionJuradaStub.BDJ_DDJ_ID,
    registro = declaracionJuradaStub
  )
}
