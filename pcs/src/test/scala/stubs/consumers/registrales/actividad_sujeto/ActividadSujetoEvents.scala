package stubs.consumers.registrales.actividad_sujeto

import consumers.registral.actividad_sujeto.domain.ActividadSujetoEvents.ActividadSujetoUpdatedFromDto
import stubs.consumers.registrales.actividad_sujeto.ActividadSujetoExternalDto.actividadSujetoStub
import stubs.consumers.registrales.calendario.CalendarioExternalDtoStub.calendarioStub

object ActividadSujetoEvents {
  def actividadSujetoUpdatedFromDtoStub = ActividadSujetoUpdatedFromDto(
    deliveryId = actividadSujetoStub.EV_ID.toInt,
    sujetoId = actividadSujetoStub.BAT_SUJ_IDENTIFICADOR,
    actividadSujetoId = actividadSujetoStub.BAT_ATD_ID,
    registro = actividadSujetoStub
  )
}
