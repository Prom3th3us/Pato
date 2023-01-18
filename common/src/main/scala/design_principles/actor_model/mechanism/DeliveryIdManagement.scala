package design_principles.actor_model.mechanism

import design_principles.actor_model.{Command, Event}

object DeliveryIdManagement {
  def isIdempotent(
      event: Event,
      command: Command,
      lastDeliveryIdByEvents: BigInt
  ): Boolean =
    command.deliveryId <= lastDeliveryIdByEvents
}
