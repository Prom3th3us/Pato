package consumers.registral.domicilio_sujeto.application.entities

import design_principles.actor_model.Command

sealed trait DomicilioSujetoCommands extends Command with DomicilioSujetoMessage

object DomicilioSujetoCommands {
  case class DomicilioSujetoUpdateFromDto(
      sujetoId: String,
      domicilioId: String,
      deliveryId: BigInt,
      registro: DomicilioSujetoExternalDto
  ) extends DomicilioSujetoCommands
}
