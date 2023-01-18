package cqrs.untyped.event

import scala.reflect.ClassTag

import design_principles.actor_model.Event

trait EventBus[P[_]] extends EventPublisher[P] {
  def subscribe[E <: Event: ClassTag](handler: E => P[Unit]): Unit
}
