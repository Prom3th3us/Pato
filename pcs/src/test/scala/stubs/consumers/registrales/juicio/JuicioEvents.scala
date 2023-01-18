package stubs.consumers.registrales.juicio

import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import stubs.consumers.registrales.juicio.JuicioCommands.detallesJuicio
import stubs.consumers.registrales.juicio.JuicioExternalDto.juicioTriStub
import stubs.consumers.registrales.parametrica_plan.ParametricaPlanExternalDto.parametricaPlanTriStub
object JuicioEvents {

  def juicioUpdatedFromDtoTriStub = JuicioUpdatedFromDto(
    deliveryId = juicioTriStub.EV_ID.toInt,
    sujetoId = juicioTriStub.BJU_SUJ_IDENTIFICADOR,
    objetoId = juicioTriStub.BJU_SOJ_IDENTIFICADOR,
    tipoObjeto = juicioTriStub.BJU_SOJ_TIPO_OBJETO,
    juicioId = juicioTriStub.BJU_JUI_ID,
    registro = juicioTriStub,
    detallesJuicio = detallesJuicio.getOrElse(Seq.empty)
  )
}
