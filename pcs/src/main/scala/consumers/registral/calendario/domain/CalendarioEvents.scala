package consumers.registral.calendario.domain

import consumers.registral.calendario.application.entities.CalendarioExternalDto
import design_principles.actor_model.Event

sealed trait CalendarioEvents extends Event
object CalendarioEvents {
  case class CalendarioUpdatedFromDto(
      deliveryId: BigInt,
      aggregateRoot: String,
      registro: CalendarioExternalDto
  ) extends CalendarioEvents
}
