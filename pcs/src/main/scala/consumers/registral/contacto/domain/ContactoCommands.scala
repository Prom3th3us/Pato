package consumers.registral.contacto.domain

sealed trait ContactoCommands extends design_principles.actor_model.Command

object ContactoCommands {
  case class ContactoUpdateFromDto(deliveryId: BigInt, aggregateRoot: String, registro: ContactoExternalDto)
      extends ContactoCommands
}
