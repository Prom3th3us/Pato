package stubs.consumers.no_registrales.sujeto

import consumers.no_registral.sujeto.domain.SujetoEvents._
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub.obligacionesTri
import stubs.consumers.no_registrales.sujeto.SujetoExternalDto._
import utils.generators.Model.deliveryId

object SujetoEvents {
  def sujetoSnapshotPersisted = SujetoSnapshotPersisted(
    deliveryId = deliveryId,
    sujetoId = "1",
    registro = None,
    saldo = 0
  )
  def sujetoUpdatedFromObjetoStub =
    SujetoUpdatedFromObjeto(
      deliveryId = deliveryId,
      sujetoId = obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      objetoId = obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      tipoObjeto = obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      saldoObjeto = 0,
      saldoObligaciones = 0
    )

  def sujetoUpdatedFromDtoAntStub =
    SujetoUpdatedFromAnt(deliveryId, sujetoAntStub.SUJ_IDENTIFICADOR, sujetoAntStub)

  def sujetoUpdatedFromDtoTriStub =
    SujetoUpdatedFromTri(deliveryId, sujetoTriStub.SUJ_IDENTIFICADOR, sujetoTriStub)

  def sujetoUpdatedFromObjetoConsolidationStub = SujetoSnapshotPersisted(
    deliveryId,
    sujetoId = obligacionesTri.BOB_SUJ_IDENTIFICADOR,
    None,
    saldo = 0
  )
}
