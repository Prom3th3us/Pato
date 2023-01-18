package cqrs.untyped.query

import design_principles.actor_model.Query
import org.slf4j.Logger

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

final class AsyncQueryBus(
    logger: Logger,
    onSuccess: String => Unit = _ => (),
    onFailure: Throwable => Unit = _ => (),
    recordLatencyInMillis: (String, Long, Long) => Unit = (_, _, _) => ()
)(implicit ec: ExecutionContext)
    extends QueryBus[Future] {

  private var handlers: Map[Class[_], Query => Future[Any]] = Map.empty

  override def ask[Q <: Query](query: Q): Future[Q#ReturnType] =
    handlers
      .get(query.getClass) match {
      case Some(handler) => handleQuery(query, handler)
      case None => Future.failed(QueryHandlerNotFound(query.getClass.getSimpleName))
    }

  override def subscribe[Q <: Query: ClassTag](handler: Q => Future[Q#ReturnType]): Unit = {
    val classTag = implicitly[ClassTag[Q]]

    synchronized {
      if (handlers.contains(classTag.runtimeClass)) {
        logger.error("handler already subscribed", "handler_name" -> handler.getClass.getSimpleName)
      } else {
        val transformed: Query => Future[Any] = (t: Query) => handler(t.asInstanceOf[Q])
        handlers = handlers + (classTag.runtimeClass -> transformed)
      }
    }
  }

  private def handleQuery[Q <: Query](query: Q, handler: Q => Future[Any]): Future[Q#ReturnType] = {
    val before = System.currentTimeMillis()
    val asyncResult = handler(query).map(_.asInstanceOf[Q#ReturnType]).map { result =>
      onSuccess(query.getClass.getSimpleName)
      recordLatencyInMillis(query.getClass.getSimpleName, before, System.currentTimeMillis())
      result
    }
    asyncResult.failed.foreach { error =>
      recordLatencyInMillis(query.getClass.getSimpleName, before, System.currentTimeMillis())
      onFailure(error)
    }
    asyncResult
  }

  private case class QueryHandlerNotFound(queryName: String) extends Exception(s"handler for $queryName not found")
}
