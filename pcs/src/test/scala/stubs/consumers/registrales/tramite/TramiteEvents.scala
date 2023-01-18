package stubs.consumers.registrales.tramite
import consumers.registral.tramite.domain.TramiteEvents.TramiteUpdatedFromDto
import stubs.consumers.no_registrales.obligacion.ObligacionEvents.obligacionUpdatedFromDtoTriStub
import stubs.consumers.registrales.tramite.TramiteExternalDto.tramiteStub

object TramiteEvents {
  def tramiteUpdatedFromDtoStub = TramiteUpdatedFromDto(
    deliveryId = tramiteStub.EV_ID.toInt,
    sujetoId = tramiteStub.BTR_SUJ_IDENTIFICADOR,
    tramiteId = tramiteStub.BTR_TRMID,
    registro = tramiteStub
  )
}
