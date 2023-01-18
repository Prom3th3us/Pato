package cassandra.write

import scala.concurrent.{ExecutionContext, Future}

import akka.Done
import akka.actor.ActorSystem
import ddd.ReadSideProjection
import design_principles.actor_model.Event

trait CassandraWrite {
  def writeState[E <: Event](state: ReadSideProjection[E])(
      implicit
      ec: ExecutionContext
  ): Future[Done]

  def cql(cql: String)(
      implicit
      ec: ExecutionContext
  ): Future[Done]
}
