package stubs.consumers.registrales.parametrica_recargo

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto
import stubs.consumers.registrales.parametrica_recargo.ParametricaRecargoExternalDto.{
  parametricaRecargoAntStub,
  parametricaRecargoTriStub
}
import utils.generators.Model.deliveryId

object ParametricaRecargoCommands {
  def parametricaPlanUpdateFromDtoAntStub: ParametricaRecargoUpdateFromDto = ParametricaRecargoUpdateFromDto(
    deliveryId = deliveryId,
    parametricaRecargoId = parametricaRecargoAntStub.BPR_INDICE,
    registro = parametricaRecargoAntStub
  )
  def parametricaPlanUpdateFromDtoTriStub: ParametricaRecargoUpdateFromDto = ParametricaRecargoUpdateFromDto(
    deliveryId = deliveryId,
    parametricaRecargoId = parametricaRecargoTriStub.BPR_INDICE,
    registro = parametricaRecargoTriStub
  )
}
