package stubs.consumers.no_registrales.sujeto

import consumers.no_registral.sujeto.application.entity.SujetoCommands._
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub.obligacionesTri
import stubs.consumers.no_registrales.sujeto.SujetoExternalDto._
import utils.generators.Model.deliveryId

object SujetoCommands {

  def sujetoUpdateFromObjetoStub =
    SujetoUpdateFromObjeto(
      deliveryId,
      sujetoId = obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      objetoId = obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      tipoObjeto = obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      saldoObjeto = 0,
      saldoObligaciones = 0
    )

  def sujetoUpdateFromDtoAntStub =
    SujetoUpdateFromAnt(deliveryId, sujetoAntStub.SUJ_IDENTIFICADOR, sujetoAntStub)

  def sujetoUpdateFromDtoTriStub =
    SujetoUpdateFromTri(deliveryId, sujetoTriStub.SUJ_IDENTIFICADOR, sujetoTriStub)

}
