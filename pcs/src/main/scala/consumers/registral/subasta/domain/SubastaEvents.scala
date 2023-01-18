package consumers.registral.subasta.domain

import consumers.registral.subasta.application.entities.{SubastaExternalDto, SubastaMessage}
import design_principles.actor_model.Event

sealed trait SubastaEvents extends Event with SubastaMessage {
  def sujetoId: String
  def objetoId: String
  def tipoObjeto: String
  def subastaId: String
}

object SubastaEvents {
  case class SubastaUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      subastaId: String,
      registro: SubastaExternalDto
  ) extends SubastaEvents
}
