package stubs.consumers.registrales.parametrica_plan

import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents.ParametricaPlanUpdatedFromDto
import stubs.consumers.registrales.parametrica_plan.ParametricaPlanExternalDto.{
  parametricaPlanAntStub,
  parametricaPlanTriStub
}
import stubs.consumers.registrales.parametrica_recargo.ParametricaRecargoExternalDto.parametricaRecargoTriStub

object ParametricaPlanEvents {
  def parametricaPlanUpdatedFromDtoAntStub = ParametricaPlanUpdatedFromDto(
    deliveryId = parametricaPlanAntStub.EV_ID.toInt,
    bppRdlId = parametricaPlanAntStub.BPP_RDL_ID,
    bppFpmId = parametricaPlanAntStub.BPP_FPM_ID,
    bppCantMaxCuotas = parametricaPlanAntStub.BPP_CANT_MAX_CUOTAS,
    bppCantMinCuotas = parametricaPlanAntStub.BPP_CANT_MIN_CUOTAS,
    bppDiasVtoCuotas = parametricaPlanAntStub.BPP_DIAS_VTO_CUOTAS,
    bppFechaDesdeDeuda = parametricaPlanAntStub.BPP_FECHA_DESDE_DEUDA,
    bppFechaFin = parametricaPlanAntStub.BPP_FECHA_FIN,
    bppFechaHastaDeuda = parametricaPlanAntStub.BPP_FECHA_HASTA_DEUDA,
    bppFechaInicio = parametricaPlanAntStub.BPP_FECHA_INICIO,
    bppFpmDescripcion = parametricaPlanAntStub.BPP_FPM_DESCRIPCION,
    bppIndiceIntFinanc = parametricaPlanAntStub.BPP_INDICE_INT_FINANC,
    bppIndiceIntPunit = parametricaPlanAntStub.BPP_INDICE_INT_PUNIT,
    bppIndiceIntResar = parametricaPlanAntStub.BPP_INDICE_INT_RESAR,
    bppMontoMaxDeuda = parametricaPlanAntStub.BPP_MONTO_MAX_DEUDA,
    bppMontoMinAnticipo = parametricaPlanAntStub.BPP_MONTO_MIN_ANTICIPO,
    bppMontoMinCuota = parametricaPlanAntStub.BPP_MONTO_MIN_CUOTA,
    bppMontoMinDeuda = parametricaPlanAntStub.BPP_MONTO_MIN_DEUDA,
    bppPorcentajeAnticipo = parametricaPlanAntStub.BPP_PORCENTAJE_ANTICIPO,
    registro = parametricaPlanAntStub
  )
  def parametricaPlanUpdatedFromDtoTriStub = ParametricaPlanUpdatedFromDto(
    deliveryId = parametricaPlanTriStub.EV_ID.toInt,
    bppRdlId = parametricaPlanTriStub.BPP_RDL_ID,
    bppFpmId = parametricaPlanTriStub.BPP_FPM_ID,
    bppCantMaxCuotas = parametricaPlanTriStub.BPP_CANT_MAX_CUOTAS,
    bppCantMinCuotas = parametricaPlanTriStub.BPP_CANT_MIN_CUOTAS,
    bppDiasVtoCuotas = parametricaPlanTriStub.BPP_DIAS_VTO_CUOTAS,
    bppFechaDesdeDeuda = parametricaPlanTriStub.BPP_FECHA_DESDE_DEUDA,
    bppFechaFin = parametricaPlanTriStub.BPP_FECHA_FIN,
    bppFechaHastaDeuda = parametricaPlanTriStub.BPP_FECHA_HASTA_DEUDA,
    bppFechaInicio = parametricaPlanTriStub.BPP_FECHA_INICIO,
    bppFpmDescripcion = parametricaPlanTriStub.BPP_FPM_DESCRIPCION,
    bppIndiceIntFinanc = parametricaPlanTriStub.BPP_INDICE_INT_FINANC,
    bppIndiceIntPunit = parametricaPlanTriStub.BPP_INDICE_INT_PUNIT,
    bppIndiceIntResar = parametricaPlanTriStub.BPP_INDICE_INT_RESAR,
    bppMontoMaxDeuda = parametricaPlanTriStub.BPP_MONTO_MAX_DEUDA,
    bppMontoMinAnticipo = parametricaPlanTriStub.BPP_MONTO_MIN_ANTICIPO,
    bppMontoMinCuota = parametricaPlanTriStub.BPP_MONTO_MIN_CUOTA,
    bppMontoMinDeuda = parametricaPlanTriStub.BPP_MONTO_MIN_DEUDA,
    bppPorcentajeAnticipo = parametricaPlanTriStub.BPP_PORCENTAJE_ANTICIPO,
    registro = parametricaPlanTriStub
  )
}
