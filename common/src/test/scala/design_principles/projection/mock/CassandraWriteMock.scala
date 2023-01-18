package design_principles.projection.mock

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

import akka.{actor, Done}
import cassandra.write.CassandraWrite
import design_principles.actor_model.Event

class CassandraWriteMock(rowsAsMap: mutable.Map[String, Map[String, String]],
                         proyectionistReaction: Any => (String, String))
    extends CassandraWrite {

  override def writeState[E <: Event](
      state: ddd.ReadSideProjection[E]
  )(implicit ec: ExecutionContext): Future[Done] = {
    val (key, value) = proyectionistReaction(state.event)
    rowsAsMap.addOne((key, Map("event" -> value)))
    Future.successful(Done)
  }

  override def cql(cql: String)(implicit ec: ExecutionContext): Future[Done] = {
    cql match {
      case s"Delete this row: $key" =>
        rowsAsMap.remove(key)
    }
    Future.successful(Done)
  }
}
