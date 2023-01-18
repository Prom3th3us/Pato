package stubs.consumers.registrales.actividad_sujeto

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoCommands.ActividadSujetoUpdateFromDto
import stubs.consumers.registrales.actividad_sujeto.ActividadSujetoExternalDto.actividadSujetoStub
import utils.generators.Model.deliveryId

object ActividadSujetoCommands {
  val actividadSujetoUpdateFromDtoStub = ActividadSujetoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = actividadSujetoStub.BAT_SUJ_IDENTIFICADOR,
    actividadSujetoId = actividadSujetoStub.BAT_ATD_ID,
    registro = actividadSujetoStub
  )
}
