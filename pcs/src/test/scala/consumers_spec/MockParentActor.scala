package consumers_spec

import akka.actor.Status.Success
import akka.actor.{Actor, ActorRef, Props}
import design_principles.actor_model.{Command, Query, Response}
import org.slf4j.{Logger, LoggerFactory}

import scala.reflect.ClassTag

class MockParentActor[ParentCommands <: Command: ClassTag, ParentResponses <: Command: ClassTag](typeName: String,
                                                                                                 childProps: Props)
    extends Actor {

  val child: ActorRef = context.actorOf(childProps, typeName)
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def receive: PartialFunction[Any, Unit] = {
    case (command: ParentCommands, replyTo: ActorRef) =>
      log.info(s"[MockParentActor-$typeName] We received a command from the child actor: $command")
      context.sender() ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

    case msg @ (command: ParentResponses, replyTo: ActorRef) =>
      log.info(s"[MockParentActor-$typeName] We received a response from the child actor: $msg")
      context.sender() ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

    case msg @ (command: Any, replyTo: ActorRef) =>
      log.info(s"[MockParentActor-$typeName] We received a Any msg from the child actor: $msg")
      context.sender() ! Success(Response.SuccessProcessing("", 0))

    case command: Command =>
      log.info(s"[MockParentActor-$typeName] Sent message to child $command")
      child forward command

    case query: Query =>
      child forward query

    case other =>
      log.info(s"[MockParentActor-$typeName] Unexpected message: $other")
  }
}
