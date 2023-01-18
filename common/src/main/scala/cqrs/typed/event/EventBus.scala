package cqrs.typed.event

import scala.reflect.ClassTag

trait EventBus[Event, State] {

  def publish(state: State, event: Event): State

  def subscribe[E <: Event: ClassTag](handler: (State, E) => State): Unit

}
