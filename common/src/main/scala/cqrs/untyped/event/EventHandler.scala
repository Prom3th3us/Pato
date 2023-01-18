package cqrs.untyped.event

import scala.concurrent.Future
import scala.util.Try

import design_principles.actor_model.Event
import org.slf4j.{Logger, LoggerFactory}

trait EventHandler[P[_], E <: Event] {
  def handle(event: E): P[Unit]
  val log: Logger = LoggerFactory.getLogger(this.getClass)

}

object EventHandler {
  trait AsyncEventHandler[E <: Event] extends EventHandler[Future, E]
  trait SyncEventHandler[E <: Event] extends EventHandler[Try, E]
}
