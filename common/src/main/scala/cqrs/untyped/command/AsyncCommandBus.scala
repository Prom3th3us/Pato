package cqrs.untyped.command

import design_principles.actor_model.Command
import org.slf4j.Logger

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class AsyncCommandBus(
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => (),
    recordLatencyInMillis: (String, Long, Long) => Unit = (_, _, _) => ()
)(implicit ec: ExecutionContext)
    extends CommandBus[Future] {

  private var handlers = Map.empty[Class[_], Command => Future[Any]]

  override def publish[C <: Command](command: C): Future[C#ReturnType] =
    handlers
      .get(command.getClass) match {
      case Some(handler) => handleCommand(command, handler)
      case None => Future.failed(CommandHandlerNotFound(command.getClass.getSimpleName))
    }

  override def subscribe[C <: Command: ClassTag](handler: C => Future[C#ReturnType]): Unit = {
    val classTag = implicitly[ClassTag[C]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: Command => Future[Any] = (t: Command) => handler(t.asInstanceOf[C])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  private def handleCommand[C <: Command](command: C, handler: C => Future[Any]): Future[C#ReturnType] = {
    val before = System.currentTimeMillis()
    val asyncResult = handler(command).map(_.asInstanceOf[C#ReturnType]).map { result =>
      onSuccess(command.getClass.getSimpleName)
      recordLatencyInMillis(command.getClass.getSimpleName, before, System.currentTimeMillis())
      result
    }
    asyncResult.failed.foreach { error =>
      recordLatencyInMillis(command.getClass.getSimpleName, before, System.currentTimeMillis())
      onFailure(error)
    }
    asyncResult
  }

  case class CommandHandlerNotFound(commandName: String) extends Exception(s"handler for $commandName not found")
}
