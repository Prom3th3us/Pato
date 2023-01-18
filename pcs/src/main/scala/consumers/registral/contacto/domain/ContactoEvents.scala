package consumers.registral.contacto.domain

sealed trait ContactoEvents extends design_principles.actor_model.Event
object ContactoEvents {
  case class ContactoUpdatedFromDto(deliveryId: BigInt, aggregateRoot: String, registro: ContactoExternalDto)
      extends ContactoEvents
}
