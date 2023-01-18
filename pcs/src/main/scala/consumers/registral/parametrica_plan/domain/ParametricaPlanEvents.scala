package consumers.registral.parametrica_plan.domain

import java.time.LocalDateTime

import consumers.registral.parametrica_plan.application.entities.{ParametricaPlanExternalDto, ParametricaPlanMessage}
import design_principles.actor_model.Event

sealed trait ParametricaPlanEvents extends Event with ParametricaPlanMessage {

  override val parametricaPlanId: String = bppFpmId

  def bppRdlId: String
  def bppFpmId: String
  def bppCantMaxCuotas: BigInt
  def bppCantMinCuotas: BigInt
  def bppDiasVtoCuotas: BigInt
  def bppFechaDesdeDeuda: LocalDateTime
  def bppFechaFin: LocalDateTime
  def bppFechaHastaDeuda: LocalDateTime
  def bppFechaInicio: LocalDateTime
  def bppFpmDescripcion: String
  def bppIndiceIntFinanc: String
  def bppIndiceIntPunit: String
  def bppIndiceIntResar: String
  def bppMontoMaxDeuda: BigDecimal
  def bppMontoMinAnticipo: BigDecimal
  def bppMontoMinCuota: BigDecimal
  def bppMontoMinDeuda: BigDecimal
  def bppPorcentajeAnticipo: BigDecimal
}
object ParametricaPlanEvents {
  case class ParametricaPlanUpdatedFromDto(
      deliveryId: BigInt,
      bppRdlId: String,
      bppFpmId: String,
      bppCantMaxCuotas: BigInt,
      bppCantMinCuotas: BigInt,
      bppDiasVtoCuotas: BigInt,
      bppFechaDesdeDeuda: LocalDateTime,
      bppFechaFin: LocalDateTime,
      bppFechaHastaDeuda: LocalDateTime,
      bppFechaInicio: LocalDateTime,
      bppFpmDescripcion: String,
      bppIndiceIntFinanc: String,
      bppIndiceIntPunit: String,
      bppIndiceIntResar: String,
      bppMontoMaxDeuda: BigDecimal,
      bppMontoMinAnticipo: BigDecimal,
      bppMontoMinCuota: BigDecimal,
      bppMontoMinDeuda: BigDecimal,
      bppPorcentajeAnticipo: BigDecimal,
      registro: ParametricaPlanExternalDto
  ) extends ParametricaPlanEvents
}
