package stubs.consumers.no_registrales.obligacion

import consumers.no_registral.obligacion.domain.ObligacionEvents._
import stubs.consumers.no_registrales.obligacion.ObligacionCommands.detallesObligaciones
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub._
import stubs.consumers.registrales.actividad_sujeto.ActividadSujetoExternalDto.actividadSujetoStub
import utils.generators.Model.deliveryId

object ObligacionEvents {
  def obligacionPersistedSnapshot = ObligacionPersistedSnapshot(
    deliveryId = obligacionUpdatedFromDtoTriStub.deliveryId.toInt,
    obligacionUpdatedFromDtoTriStub.sujetoId,
    obligacionUpdatedFromDtoTriStub.objetoId,
    obligacionUpdatedFromDtoTriStub.tipoObjeto,
    obligacionUpdatedFromDtoTriStub.obligacionId,
    registro = Some(stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub.obligacionesTri),
    exenta = false,
    porcentajeExencion = 0,
    saldo = 0,
    operacion = "U"
  )
  def obligacionUpdatedFromDtoTriStub =
    ObligacionUpdatedFromDto(
      deliveryId = deliveryId,
      obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      obligacionesTri.BOB_OBN_ID,
      obligacionesTri,
      detallesObligaciones.getOrElse(Seq.empty),
      isAdheridoDebito = None
    )
  def obligacionUpdatedFromDtoAntStub =
    ObligacionUpdatedFromDto(
      deliveryId = deliveryId,
      obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      obligacionesTri.BOB_OBN_ID,
      obligacionesAnt,
      detallesObligaciones.getOrElse(Seq.empty),
      isAdheridoDebito = None
    )
  def obligacionRemovedStub = ObligacionRemoved(
    deliveryId = deliveryId,
    obligacionesTri.BOB_SUJ_IDENTIFICADOR,
    obligacionesTri.BOB_SOJ_IDENTIFICADOR,
    obligacionesTri.BOB_SOJ_TIPO_OBJETO,
    obligacionesTri.BOB_OBN_ID,
    registro = obligacionesTri,
    cuota = None
  )

}
