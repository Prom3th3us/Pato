package consumers.registral.domicilio_objeto.domain

import consumers.registral.domicilio_objeto.application.entities.{DomicilioObjetoExternalDto, DomicilioObjetoMessage}
import design_principles.actor_model.Event

sealed trait DomicilioObjetoEvents extends Event with DomicilioObjetoMessage

object DomicilioObjetoEvents {
  case class DomicilioObjetoUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      domicilioId: String,
      registro: DomicilioObjetoExternalDto
  ) extends DomicilioObjetoEvents

}
