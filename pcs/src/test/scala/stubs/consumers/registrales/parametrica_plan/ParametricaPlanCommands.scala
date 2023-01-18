package stubs.consumers.registrales.parametrica_plan

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanCommands.ParametricaPlanUpdateFromDto
import stubs.consumers.registrales.parametrica_plan.ParametricaPlanExternalDto.{
  parametricaPlanAntStub,
  parametricaPlanTriStub
}
import utils.generators.Model.deliveryId

object ParametricaPlanCommands {
  def parametricaPlanUpdateFromDtoAntStub: ParametricaPlanUpdateFromDto = ParametricaPlanUpdateFromDto(
    deliveryId = deliveryId,
    parametricaPlanId = parametricaPlanAntStub.BPP_FPM_ID,
    registro = parametricaPlanAntStub
  )
  def parametricaPlanUpdateFromDtoTriStub: ParametricaPlanUpdateFromDto = ParametricaPlanUpdateFromDto(
    deliveryId = deliveryId,
    parametricaPlanId = parametricaPlanTriStub.BPP_FPM_ID,
    registro = parametricaPlanTriStub
  )
}
