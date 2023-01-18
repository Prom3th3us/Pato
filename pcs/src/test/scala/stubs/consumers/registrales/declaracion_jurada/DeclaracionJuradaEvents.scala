package stubs.consumers.registrales.declaracion_jurada

import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto
import stubs.consumers.registrales.declaracion_jurada.DeclaracionJuradaExternalDto.declaracionJuradaStub

object DeclaracionJuradaEvents {
  def declaracionJuradaUpdatedFromDtoStub = DeclaracionJuradaUpdatedFromDto(
    deliveryId = declaracionJuradaStub.EV_ID.toInt,
    sujetoId = declaracionJuradaStub.BDJ_SUJ_IDENTIFICADOR,
    objetoId = declaracionJuradaStub.BDJ_SOJ_IDENTIFICADOR,
    tipoObjeto = declaracionJuradaStub.BDJ_SOJ_TIPO_OBJETO,
    declaracionJuradaId = declaracionJuradaStub.BDJ_DDJ_ID,
    registro = declaracionJuradaStub
  )
}
