package stubs.consumers.registrales.plan_pago

import consumers.registral.plan_pago.domain.PlanPagoEvents.PlanPagoUpdatedFromDto
import stubs.consumers.registrales.plan_pago.PlanPagoExternalDto.{planPagoAntStub, planPagoTriStub}
import stubs.consumers.registrales.subasta.SubastaExternalDtoStub.subastaStub

object PlanPagoEvents {
  def PlanPagoUpdatedFromDtoAntStub = PlanPagoUpdatedFromDto(
    deliveryId = planPagoAntStub.EV_ID.toInt,
    sujetoId = planPagoAntStub.BPL_SUJ_IDENTIFICADOR,
    objetoId = planPagoAntStub.BPL_SOJ_IDENTIFICADOR,
    tipoObjeto = planPagoAntStub.BPL_SOJ_TIPO_OBJETO,
    planPagoId = planPagoAntStub.BPL_PLN_ID,
    registro = planPagoAntStub
  )
  def PlanPagoUpdatedFromDtoTriStub = PlanPagoUpdatedFromDto(
    deliveryId = planPagoTriStub.EV_ID.toInt,
    sujetoId = planPagoTriStub.BPL_SUJ_IDENTIFICADOR,
    objetoId = planPagoTriStub.BPL_SOJ_IDENTIFICADOR,
    tipoObjeto = planPagoTriStub.BPL_SOJ_TIPO_OBJETO,
    planPagoId = planPagoTriStub.BPL_PLN_ID,
    registro = planPagoTriStub
  )
}
