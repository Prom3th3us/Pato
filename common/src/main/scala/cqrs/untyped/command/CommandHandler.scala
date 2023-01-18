package cqrs.untyped.command

import design_principles.actor_model.Command
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future
import scala.util.Try

trait CommandHandler[P[_], C <: Command] {
  def handle(command: C): P[C#ReturnType]
  val log: Logger = LoggerFactory.getLogger(this.getClass)

}

object CommandHandler {
  trait AsyncCommandHandler[C <: Command] extends CommandHandler[Future, C]
  trait SyncCommandHandler[C <: Command] extends CommandHandler[Try, C]
}
