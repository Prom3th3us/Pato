package cqrs.typed.query

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.ReplyEffect
import design_principles.actor_model.Query

import scala.reflect.ClassTag

trait QueryBus[Event, State] {
  type AkkaEffect = ReplyEffect[Event, State]

  def ask[Q <: Query](state: State, query: Q)(replyTo: ActorRef[Query#ReturnType]): AkkaEffect

  def subscribe[Q <: Query: ClassTag](handler: Q => State => ActorRef[Q#ReturnType] => AkkaEffect): Unit

}
