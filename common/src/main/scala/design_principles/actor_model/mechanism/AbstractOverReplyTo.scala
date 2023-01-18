package design_principles.actor_model.mechanism

/*
The following mechanism allows the user to abstract over the replyTo: ActorRef[_] in AkkaTyped

The idea is to surround the desired payload with a wrapper which contains
 an automatically inferred replyTo: ActorRef[_]

 To see usages see:  design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk

 */
object AbstractOverReplyTo {
  case class MessageWithAutomaticReplyTo[Payload <: design_principles.actor_model.ShardedMessage, Response](
      payload: Payload,
      replyTo: akka.actor.typed.ActorRef[Response]
  ) extends design_principles.actor_model.ShardedMessage {
    override def aggregateRoot: String = payload.aggregateRoot
  }
}
