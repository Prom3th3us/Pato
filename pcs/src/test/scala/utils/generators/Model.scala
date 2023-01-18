package utils.generators

import consumers.no_registral.objeto.application.entities.ObjetoCommands.ObjetoUpdateFromObligacion
import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoUpdatedFromObligacion
import consumers.no_registral.sujeto.domain.SujetoEvents.SujetoUpdatedFromObjeto

object Model {

  var usedEntityIds: Set[String] = Set.empty
  def randomEntityId: String = {
    val entityId = utils.generators.Numbers.positiveNumber.toString
    if (usedEntityIds contains entityId) {
      randomEntityId
    } else {
      usedEntityIds = usedEntityIds + entityId
      entityId
    }
  }

  def sujetoId = randomEntityId
  def objetoId = randomEntityId
  def tipoObjeto = randomEntityId
  def obligacionId = randomEntityId

  def obligacionObjetoUpdated(
      objetoId: Int,
      obligacionId: String,
      sujetoId: String,
      saldo: BigDecimal,
      vencida: Boolean = false
  ): ObjetoUpdatedFromObligacion =
    ObjetoUpdatedFromObligacion(
      sujetoId = sujetoId,
      objetoId = objetoId.toString,
      objetoId2 = None,
      tipoObjeto = "I",
      obligacionId = obligacionId,
      saldoObligacion = saldo,
      obligacionExenta = false,
      porcentajeExencion = None,
      deliveryId = deliveryId,
      idExterno = None
    )

  def objetoSujetoUpdated(
      offset: Int,
      sujetoId: String,
      objetoId: String,
      saldo: BigDecimal,
      vencida: Boolean = false
  ) =
    SujetoUpdatedFromObjeto(
      deliveryId,
      sujetoId,
      objetoId = objetoId,
      tipoObjeto = "I",
      saldoObjeto = saldo,
      saldoObligaciones = 0
    )

  def deliveryId: Int = utils.generators.Numbers.positiveNumber

  def obligacionTri(offset: Int,
                    obligacionId: String,
                    objetoId: String,
                    sujetoId: String,
                    saldoObligacion: BigDecimal,
                    vencida: Boolean = false) =
    ObjetoUpdateFromObligacion(
      sujetoId = sujetoId,
      objetoId = objetoId,
      objetoId2 = None,
      tipoObjeto = "I",
      deliveryId = deliveryId,
      obligacionId = obligacionId,
      saldoObligacion = saldoObligacion,
      obligacionExenta = false,
      porcentajeExencion = None,
      idExterno = None
    )

}
