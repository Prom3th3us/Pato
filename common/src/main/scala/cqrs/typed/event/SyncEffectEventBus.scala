package cqrs.typed.event

import scala.reflect.ClassTag

final class SyncEffectEventBus[Event, State]() extends EventBus[Event, State] {

  type EventHandler = (State, Event) => State
  private var handlers: Map[Class[_], List[EventHandler]] = Map.empty

  def publish(state: State, event: Event): State =
    handlers
      .getOrElse(event.getClass, List.empty[EventHandler])
      .foldLeft(state) { (previousState, handler) =>
        handler(previousState, event)
      }

  def subscribe[E <: Event: ClassTag](handler: (State, E) => State): Unit = {
    val classTag = implicitly[ClassTag[E]]

    val transformed: EventHandler = (s: State, t: Event) => handler(s, t.asInstanceOf[E])

    val updatedEventHandlers: List[EventHandler] = handlers
        .getOrElse(classTag.runtimeClass, List.empty[EventHandler]) :+ transformed

    handlers = handlers + (classTag.runtimeClass -> updatedEventHandlers)
  }

}
