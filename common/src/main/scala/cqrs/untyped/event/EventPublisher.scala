package cqrs.untyped.event

import design_principles.actor_model.Event

trait EventPublisher[P[_]] {
  def publish[E <: Event](event: E): P[Unit]
}
