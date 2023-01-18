package consumers.registral.domicilio_objeto.application.entities

import design_principles.actor_model.Command

sealed trait DomicilioObjetoCommands extends Command with DomicilioObjetoMessage

object DomicilioObjetoCommands {
  case class DomicilioObjetoUpdateFromDto(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      domicilioId: String,
      deliveryId: BigInt,
      registro: DomicilioObjetoExternalDto
  ) extends DomicilioObjetoCommands
}
