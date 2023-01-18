package design_principles.actor_model.mechanism

import akka.util.Timeout
import cqrs.base_actor.typed.{AbstractStateWithCQRS, BasePersistentShardedTypedActorWithCQRS}
import design_principles.actor_model.mechanism.AbstractOverReplyTo.MessageWithAutomaticReplyTo

import scala.concurrent.Future
import scala.reflect.ClassTag

/*
This mechanism allows the user to expect a return type for an actor Ask.

There are two implementations, one for AkkaClassic and one for AkkaTyped.
--------------------------------------------------------------------------

1. AkkaClassic:
  import design_principles.actor_model.TypedAsk.AkkaClassicTypedAsk
  for {
      _: Response.SuccessProcessing <- actorRef.Ask[Done](command)
  }

2. AkkaTyped:
  import design_principles.actor_model.TypedAsk.AkkaTypedTypedAsk
  for {
    _: Response.SuccessProcessing <- actorRef.Ask(command)
  }
-------------------------------------------------------------------------
The second one provides much stronger guarantees, and it does so at compile time.

 */
sealed trait TypedAsk

object TypedAsk {

  /*
  This mechanism allows the user to express in the following manner:

  for {
    _: Response.SuccessProcessing <- actorRef.Ask[Done](command)
  }

  However AkkaClassic does not provide type guarantees, so if the Actor
  does not answer the expected type the Future will fail.

   */
  implicit class AkkaClassicTypedAsk(actorRef: akka.actor.ActorRef) extends TypedAsk {
    import scala.concurrent.duration._
    implicit val timeout: Timeout = Timeout(20 seconds)
    import akka.pattern.{ask => classicAsk}
    def ask[Response: ClassTag](command: Any): Future[Response] =
      (actorRef ? command).mapTo[Response]
  }

  /*
  This mechanism allows the user to express in the following manner:

  for {
    _: Response.SuccessProcessing <- actorRef.Ask(command)
  }

  Because AkkaTyped does provide type guarantees:

  If the message is not expected by the Actor, the following compiler error will ensue:
  Error:(51, 71) inferred type arguments [String] do not conform to method Ask's type parameter bounds
  [Message <: consumers.registral.actividad_sujeto.application.entities.ActividadSujetoMessage]

  And because the message itself must extend from our beautiful design_principles.actor_model.ShardedMessage,
  then the message itself contains information about it's expected return type,
  which the Actor is guaranteed to obey.

  Thus at compile time we can ask the compiler about the return type of the Ask,
  or try to expect another thing and see the IDE hit us with an error like the following:

  Pattern type is incompatible with expected type, found: String, required: GetStateActividadSujeto#ReturnType
   */
  implicit class AkkaTypedTypedAsk[
      ActorMessages <: design_principles.actor_model.ShardedMessage: ClassTag,
      ActorEvents,
      State <: AbstractStateWithCQRS[ActorMessages, ActorEvents, State]
  ](
      actor: BasePersistentShardedTypedActorWithCQRS[
        ActorMessages,
        ActorEvents,
        State
      ]
  ) //(implicit system: akka.actor.typed.ActorSystem[_])
      extends TypedAsk {

    import scala.concurrent.duration._
    implicit val timeout: Timeout = Timeout(20 seconds)

    def ask[Message <: ActorMessages](
        message: Message
    ): Future[Message#ReturnType] =
      actor
        .getEntityRefTyped[Message, Message#ReturnType](
          message.aggregateRoot
        )
        .ask[Message#ReturnType] { ref: akka.actor.typed.ActorRef[Message#ReturnType] =>
          MessageWithAutomaticReplyTo(
            message,
            ref
          )
        }
  }

}
