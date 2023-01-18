package cqrs.untyped.command

import design_principles.actor_model.Command
import org.slf4j.Logger

import scala.reflect.ClassTag
import scala.util.{Failure, Try}

class SyncCommandBus(
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => ()
) extends CommandBus[Try] {

  private var handlers = Map.empty[Class[_], Command => Try[Any]]

  override def publish[C <: Command](command: C): Try[C#ReturnType] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handleCommand(command, handler)
      case None => Failure(CommandHandlerNotFound(command.getClass.getSimpleName))
    }

  override def subscribe[C <: Command: ClassTag](handler: C => Try[C#ReturnType]): Unit = {
    val classTag = implicitly[ClassTag[C]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
    } else {
      val transformed: Command => Try[Any] = (t: Command) => handler(t.asInstanceOf[C])
      handlers = handlers + (classTag.runtimeClass -> transformed)
    }
  }

  private def handleCommand[C <: Command](command: C, handler: C => Try[Any]): Try[C#ReturnType] = {
    val result = handler(command).map(_.asInstanceOf[C#ReturnType]).map { result =>
      onSuccess(command.getClass.getSimpleName)
      result
    }
    result.failed.foreach(onFailure)
    result
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
