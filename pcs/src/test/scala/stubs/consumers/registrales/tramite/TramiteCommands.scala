package stubs.consumers.registrales.tramite

import consumers.registral.tramite.application.entities.TramiteCommands.TramiteUpdateFromDto
import stubs.consumers.registrales.tramite.TramiteExternalDto.tramiteStub
import utils.generators.Model.deliveryId

object TramiteCommands {
  def tramiteUpdateFromDtoStub: TramiteUpdateFromDto = TramiteUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = tramiteStub.BTR_SUJ_IDENTIFICADOR,
    tramiteId = tramiteStub.BTR_TRMID,
    registro = tramiteStub
  )
}
