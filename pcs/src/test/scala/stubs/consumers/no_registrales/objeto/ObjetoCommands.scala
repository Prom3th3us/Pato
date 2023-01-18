package stubs.consumers.no_registrales.objeto

import consumers.no_registral.objeto.application.entities.ObjetoCommands._
import stubs.consumers.no_registrales.objeto.ObjetoExternalDto._
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub._
import utils.generators.Model.deliveryId

object ObjetoCommands {

  def objetoUpdateFromObligacionStub =
    ObjetoUpdateFromObligacion(
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

  def objetoUpdateFromDtoTriStub =
    consumers.no_registral.objeto.application.entities.ObjetoCommands.ObjetoUpdateFromTri(
      sujetoId = objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoTriStub.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoTriStub.SOJ_TIPO_OBJETO,
      registro = objetoTriStub,
      isResponsable = Some(false),
      sujetoResponsable = Some(""),
      deliveryId = deliveryId,
      isAdheridoDebito = None
    )

  def ObjetoBajaTriFromDtoStub =
    consumers.no_registral.objeto.application.entities.ObjetoCommands.SetBajaObjeto(
      sujetoId = objetoBajaTriStubDto.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoBajaTriStubDto.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoBajaTriStubDto.SOJ_TIPO_OBJETO,
      deliveryId = deliveryId,
      registro = objetoBajaTriStubDto,
      isResponsable = Some(false),
      sujetoResponsable = Some("")
    )

  def objetoUpdatedFromDtoAntStub =
    consumers.no_registral.objeto.application.entities.ObjetoCommands.ObjetoUpdateFromAnt(
      sujetoId = objetoAntStub.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoAntStub.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoAntStub.SOJ_TIPO_OBJETO,
      deliveryId = deliveryId,
      registro = objetoAntStub
    )

  def ObjetoBajaAntFromDtoStub =
    consumers.no_registral.objeto.application.entities.ObjetoCommands.SetBajaObjeto(
      sujetoId = objetoBajaAntStubDto.SOJ_SUJ_IDENTIFICADOR,
      objetoId = objetoBajaAntStubDto.SOJ_IDENTIFICADOR,
      tipoObjeto = objetoBajaAntStubDto.SOJ_TIPO_OBJETO,
      deliveryId = deliveryId,
      registro = objetoBajaAntStubDto,
      isResponsable = None,
      sujetoResponsable = None
    )

  def objetoTagAddStub = ObjetoTagAdd(
    deliveryId,
    objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
    objetoTriStub.SOJ_IDENTIFICADOR,
    objetoTriStub.SOJ_TIPO_OBJETO,
    tag = ""
  )

  def objetoTagRemoveStub = ObjetoTagRemove(
    deliveryId,
    objetoTriStub.SOJ_SUJ_IDENTIFICADOR,
    objetoTriStub.SOJ_IDENTIFICADOR,
    objetoTriStub.SOJ_TIPO_OBJETO,
    tag = ""
  )

  def objetoAddExencionStub = ObjetoAddExencion(
    deliveryId,
    objetoExencionStub.BEX_SUJ_IDENTIFICADOR,
    objetoExencionStub.BEX_SOJ_IDENTIFICADOR,
    objetoExencionStub.BEX_SOJ_TIPO_OBJETO,
    objetoExencionStub
  )

}
