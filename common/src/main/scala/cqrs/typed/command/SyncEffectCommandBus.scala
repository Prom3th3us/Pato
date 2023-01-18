package cqrs.typed.command

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import design_principles.actor_model.{Command, Response}
import org.slf4j.Logger

import scala.reflect.ClassTag

class SyncEffectCommandBus[Event, State](
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => ()
) extends CommandBus[Event, State] {

  private var handlers = Map.empty[Class[_], Command => ActorRef[Success] => AkkaEffect]

  def publish[C <: Command, Response](command: C)(replyTo: ActorRef[Success]): AkkaEffect =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handleCommand(command, handler)(replyTo)
      case None => Effect.none
    }

  def subscribe[C <: Command: ClassTag](handler: C => ActorRef[Success] => AkkaEffect): Unit = {
    val classTag = implicitly[ClassTag[C]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
    } else {
      val transformed = (t: Command) => handler(t.asInstanceOf[C])
      handlers = handlers + (classTag.runtimeClass -> transformed)
    }
  }

  private def handleCommand[C <: Command](
      command: C,
      handler: C => ActorRef[Success] => AkkaEffect
  ): ActorRef[Success] => AkkaEffect =
    handler(command)

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
