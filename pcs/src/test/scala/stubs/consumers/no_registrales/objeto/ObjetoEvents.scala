package stubs.consumers.no_registrales.objeto

import consumers.no_registral.objeto.domain.ObjetoEvents._
import stubs.consumers.no_registrales.objeto.ObjetoExternalDto._
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub._
import utils.generators.Model.deliveryId

object ObjetoEvents {

  def objetoUpdatedFromObligacionConsolidationStub =
    consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoUpdatedFromObligacion(
      sujetoId = obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      objetoId = obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      objetoId2 = None,
      tipoObjeto = obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      obligacionId = obligacionesTri.BOB_OBN_ID,
      saldoObligacion = 0,
      obligacionExenta = false,
      porcentajeExencion = None,
      deliveryId = deliveryId,
      idExterno = None
    )

  def objetoUpdatedFromObligacionStub =
    ObjetoUpdatedFromObligacion(
      sujetoId = obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      objetoId = obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      objetoId2 = None,
      tipoObjeto = obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      obligacionId = obligacionesTri.BOB_OBN_ID,
      saldoObligacion = 0,
      obligacionExenta = false,
      porcentajeExencion = None,
      deliveryId = deliveryId,
      idExterno = None
    )

  def objetoSnapshotPersistedStub =
    consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted(
      deliveryId,
      sujetoId = objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoTriStub.SOJ_IDENTIFICADOR,
      objetoId2 = None,
      tipoObjeto = objetoTriStub.SOJ_TIPO_OBJETO,
      registro = Some(objetoTriStub),
      saldo = 0,
      cotitulares = Set.empty,
      tags = Set.empty,
      sujetoResponsable = Some(objetoTriStub.SOJ_SUJ_IDENTIFICADOR),
      porcentajeResponsabilidad = 0,
      obligacionesSaldo = Map.empty,
      cuotas = List.empty,
      operacion = "operacion",
      idExterno = None
    )

  def objetoUpdatedFromDtoTriStub =
    consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoUpdatedFromTri(
      sujetoId = objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoTriStub.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoTriStub.SOJ_TIPO_OBJETO,
      registro = objetoTriStub,
      isResponsable = Some(false),
      sujetoResponsable = Some(""),
      deliveryId = deliveryId,
      isAdheridoDebito = None
    )

  def objetoUpdatedFromDtoAntStub =
    consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoUpdatedFromAnt(
      sujetoId = objetoAntStub.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoAntStub.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoAntStub.SOJ_TIPO_OBJETO,
      registro = objetoAntStub,
      deliveryId = deliveryId
    )

  def objetoTagAddedStub = ObjetoTagAdded(
    deliveryId,
    objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
    objetoTriStub.SOJ_IDENTIFICADOR,
    objetoTriStub.SOJ_TIPO_OBJETO,
    tagAdded = ""
  )

  def objetoTagRemovedStub = ObjetoTagRemoved(
    deliveryId,
    objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
    objetoTriStub.SOJ_IDENTIFICADOR,
    objetoTriStub.SOJ_TIPO_OBJETO,
    tagRemoved = ""
  )

  val objetoAddedExencionStub = ObjetoAddedExencion(
    deliveryId,
    objetoExencionStub.BEX_SUJ_IDENTIFICADOR,
    objetoExencionStub.BEX_SOJ_IDENTIFICADOR,
    objetoExencionStub.BEX_SOJ_TIPO_OBJETO,
    objetoExencionStub
  )

  def objetoBajaTriStub = ObjetoBajaSet(
    deliveryId,
    objetoBajaTriStubDto.SOJ_SUJ_IDENTIFICADOR,
    objetoBajaTriStubDto.SOJ_IDENTIFICADOR,
    objetoBajaTriStubDto.SOJ_TIPO_OBJETO,
    objetoBajaTriStubDto,
    isResponsable = Some(false),
    sujetoResponsable = Some("")
  )

  def objetoBajaAntStub = ObjetoBajaSet(
    deliveryId,
    objetoBajaAntStubDto.SOJ_SUJ_IDENTIFICADOR,
    objetoBajaAntStubDto.SOJ_IDENTIFICADOR,
    objetoBajaAntStubDto.SOJ_TIPO_OBJETO,
    objetoBajaAntStubDto,
    None,
    None
  )

}
