package consumers.registral.declaracion_jurada.domain

import consumers.registral.declaracion_jurada.application.entities.{
  DeclaracionJuradaExternalDto,
  DeclaracionJuradaMessage
}
import design_principles.actor_model.Event

sealed trait DeclaracionJuradaEvents extends Event with DeclaracionJuradaMessage {
  def sujetoId: String
  def objetoId: String
  def tipoObjeto: String
  def declaracionJuradaId: String
}

object DeclaracionJuradaEvents {
  case class DeclaracionJuradaUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      declaracionJuradaId: String,
      registro: DeclaracionJuradaExternalDto
  ) extends DeclaracionJuradaEvents

}
