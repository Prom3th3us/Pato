package cqrs.untyped.query

import design_principles.actor_model.Query
import org.slf4j.Logger

import scala.reflect.ClassTag
import scala.util.{Failure, Try}

final class SyncQueryBus(
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => ()
) extends QueryBus[Try] {

  private var handlers: Map[Class[_], Query => Try[Any]] = Map.empty

  override def ask[Q <: Query](query: Q): Try[Q#ReturnType] =
    handlers
      .get(query.getClass) match {
      case Some(handler) => handleQuery(query, handler)
      case None => Failure(QueryHandlerNotFound(query.getClass.getSimpleName))
    }

  override def subscribe[Q <: Query: ClassTag](handler: Q => Try[Q#ReturnType]): Unit = {
    val classTag = implicitly[ClassTag[Q]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
    } else {
      val transformed: Query => Try[Any] = (t: Query) => handler(t.asInstanceOf[Q])
      handlers = handlers + (classTag.runtimeClass -> transformed)
    }
  }

  private def handleQuery[Q <: Query](query: Q, handler: Q => Try[Any]): Try[Q#ReturnType] = {
    val result = handler(query).map(_.asInstanceOf[Q#ReturnType]).map { result =>
      onSuccess(query.getClass.getSimpleName)
      result
    }
    result.failed.foreach(onFailure)
    result
  }

  private case class QueryHandlerNotFound(queryName: String) extends Exception(s"handler for $queryName not found")
}
