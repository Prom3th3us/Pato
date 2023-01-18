package cqrs.typed.query

import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import design_principles.actor_model.Query
import org.slf4j.Logger

import scala.reflect.ClassTag

final class SyncEffectQueryBus[Event, State](
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => ()
) extends QueryBus[Event, State] {

  private var handlers: Map[Class[_], Query => State => ActorRef[Query#ReturnType] => AkkaEffect] = Map.empty

  def ask[Q <: Query](state: State, query: Q)(replyTo: ActorRef[Query#ReturnType]): AkkaEffect =
    handlers
      .get(query.getClass) match {
      case Some(handler) =>
        handleQuery[Q](query, state, handler)(replyTo)
      case None =>
        Effect.noReply
    }

  def subscribe[Q <: Query: ClassTag](handler: Q => State => ActorRef[Q#ReturnType] => AkkaEffect): Unit = {
    val classTag = implicitly[ClassTag[Q]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
    } else {
      val transformed = (t: Query) => handler(t.asInstanceOf[Q])
      handlers = handlers + (classTag.runtimeClass -> transformed)
    }
  }

  private def handleQuery[Q <: Query](query: Q,
                                      state: State,
                                      handler: Query => State => ActorRef[Query#ReturnType] => AkkaEffect)(
      replyTo: ActorRef[Query#ReturnType]
  ): AkkaEffect =
    handler(query)(state)(replyTo)

  private case class QueryHandlerNotFound(queryName: String) extends Exception(s"handler for $queryName not found")
}
