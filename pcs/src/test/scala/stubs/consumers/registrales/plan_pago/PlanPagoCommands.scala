package stubs.consumers.registrales.plan_pago

import consumers.registral.plan_pago.application.entities.PlanPagoCommands.PlanPagoUpdateFromDto
import stubs.consumers.registrales.plan_pago.PlanPagoExternalDto.{planPagoAntStub, planPagoTriStub}
import utils.generators.Model.deliveryId

object PlanPagoCommands {
  def planPagoUpdateFromDtoAntStub: PlanPagoUpdateFromDto = PlanPagoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = planPagoAntStub.BPL_SUJ_IDENTIFICADOR,
    objetoId = planPagoAntStub.BPL_SOJ_IDENTIFICADOR,
    tipoObjeto = planPagoAntStub.BPL_SOJ_TIPO_OBJETO,
    planPagoId = planPagoAntStub.BPL_PLN_ID,
    registro = planPagoAntStub
  )
  def planPagoUpdateFromDtoTriStub: PlanPagoUpdateFromDto = PlanPagoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = planPagoTriStub.BPL_SUJ_IDENTIFICADOR,
    objetoId = planPagoTriStub.BPL_SOJ_IDENTIFICADOR,
    tipoObjeto = planPagoTriStub.BPL_SOJ_TIPO_OBJETO,
    planPagoId = planPagoTriStub.BPL_PLN_ID,
    registro = planPagoTriStub
  )
}
