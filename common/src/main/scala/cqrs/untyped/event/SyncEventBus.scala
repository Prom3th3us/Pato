package cqrs.untyped.event

import scala.reflect.ClassTag
import scala.util.Try

import design_principles.actor_model.Event
import org.slf4j.Logger

final class SyncEventBus(
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => (),
    recordLatencyInMillis: (String, Long, Long) => Unit = (_, _, _) => ()
) extends EventBus[Try] {

  private var handlers: Map[Class[_], List[Event => Try[Unit]]] = Map.empty

  override def publish[E <: Event](event: E): Try[Unit] = {
    val results = handlers.getOrElse(event.getClass, List.empty[Event => Try[Unit]]).map { handler =>
      handleEvent(event, handler)
    }
    Try(results).map(_ => ())
  }

  override def subscribe[E <: Event: ClassTag](handler: E => Try[Unit]): Unit = {
    val classTag = implicitly[ClassTag[E]]

    val transformed: Event => Try[Unit] = (t: Event) => handler(t.asInstanceOf[E])

    val updatedEventHandlers: List[Event => Try[Unit]] = handlers
        .getOrElse(classTag.runtimeClass, List.empty[Event => Try[Unit]]) :+ transformed

    handlers = handlers + (classTag.runtimeClass -> updatedEventHandlers)
  }

  private def handleEvent[E <: Event](event: E, handler: E => Try[Unit]): Try[Unit] = {
    val before = System.currentTimeMillis()
    val result = handler(event).map { result =>
      onSuccess(event.getClass.getSimpleName)
      recordLatencyInMillis(event.getClass.getSimpleName, before, System.currentTimeMillis())
      result
    }
    result.failed.foreach { error =>
      recordLatencyInMillis(event.getClass.getSimpleName, before, System.currentTimeMillis())
      onFailure(error)
    }
    result
  }
}
