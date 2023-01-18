package cqrs.untyped.event

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import design_principles.actor_model.Event

final class AsyncEventBus(
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => (),
    recordLatencyInMillis: (String, Long, Long) => Unit = (_, _, _) => ()
)(implicit ec: ExecutionContext)
    extends EventBus[Future] {

  private var handlers: Map[Class[_], List[Event => Future[Unit]]] = Map.empty

  override def publish[E <: Event](event: E): Future[Unit] = {
    val results = handlers.getOrElse(event.getClass, List.empty[Event => Future[Unit]]).map { handler =>
      handleEvent(event, handler)
    }
    Future.sequence(results).map(_ => ())
  }

  override def subscribe[E <: Event: ClassTag](handler: E => Future[Unit]): Unit = {
    val classTag = implicitly[ClassTag[E]]

    synchronized {
      val transformed: Event => Future[Unit] = (t: Event) => handler(t.asInstanceOf[E])

      val updatedEventHandlers: List[Event => Future[Unit]] = handlers
          .getOrElse(classTag.runtimeClass, List.empty[Event => Future[Unit]]) :+ transformed

      handlers = handlers + (classTag.runtimeClass -> updatedEventHandlers)
    }
  }

  private def handleEvent[E <: Event](event: E, handler: E => Future[Unit]): Future[Unit] = {
    val before = System.currentTimeMillis()
    val asyncResult = handler(event).map { result =>
      onSuccess(event.getClass.getSimpleName)
      recordLatencyInMillis(event.getClass.getSimpleName, before, System.currentTimeMillis())
      result
    }
    asyncResult.failed.foreach { error =>
      recordLatencyInMillis(event.getClass.getSimpleName, before, System.currentTimeMillis())
      onFailure(error)
    }
    asyncResult
  }
}
