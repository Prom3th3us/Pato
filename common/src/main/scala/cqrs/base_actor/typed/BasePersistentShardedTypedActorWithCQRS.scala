package cqrs.base_actor.typed

import scala.reflect.ClassTag
import akka.actor.Status.Success
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.persistence.typed.scaladsl.Effect
import com.typesafe.config.Config
import cqrs.typed.command.SyncEffectCommandBus
import cqrs.typed.event.SyncEffectEventBus
import cqrs.typed.query.SyncEffectQueryBus
import design_principles.actor_model.mechanism.AbstractOverReplyTo.MessageWithAutomaticReplyTo
import design_principles.actor_model.{Command, Query}
import org.slf4j.{Logger, LoggerFactory}

abstract class BasePersistentShardedTypedActorWithCQRS[
    ActorMessages <: design_principles.actor_model.ShardedMessage: ClassTag,
    ActorEvents,
    State <: AbstractStateWithCQRS[ActorMessages, ActorEvents, State]
](s: State)(implicit system: ActorSystem[_])
    extends BasePersistentShardedTypedActor[MessageWithAutomaticReplyTo[ActorMessages, ActorMessages#ReturnType],
                                            ActorEvents,
                                            State](s) {

  val logger: Logger = LoggerFactory.getLogger(getClass)
  val commandBus = new SyncEffectCommandBus[ActorEvents, State](logger)
  val queryBus = new SyncEffectQueryBus[ActorEvents, State](logger)
  val eventBus = new SyncEffectEventBus[ActorEvents, State]()

  override final def commandHandler(
      state: State,
      command: MessageWithAutomaticReplyTo[ActorMessages, ActorMessages#ReturnType]
  ): Effect[ActorEvents, State] = {
    command.payload match {
      case query: Query =>
        queryBus.ask(state, query)(command.replyTo.asInstanceOf[ActorRef[Query#ReturnType]])
      case cmd: Command =>
        commandBus.publish(cmd)(command.replyTo.asInstanceOf[ActorRef[Success]])

    }
  }

  override def eventHandler(state: State, event: ActorEvents): State =
    eventBus.publish(state, event)
}
