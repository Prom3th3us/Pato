package design_principles.projection.infrastructure

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry}
import cassandra.read.CassandraReadProduction
import cassandra.write.CassandraWriteProduction
import design_principles.projection.CassandraTestkit

class CassandraTestkitProduction()(implicit system: ActorSystem, session: CassandraSession, ec: ExecutionContext)
    extends CassandraTestkit {
  override val cassandraWrite: CassandraWriteProduction = new CassandraWriteProduction()
  override val cassandraRead: CassandraReadProduction = new CassandraReadProduction()
}
object CassandraTestkitProduction {
  def apply()(implicit system: ActorSystem, ec: ExecutionContext): CassandraTestkitProduction = {
    val sessionSettings = CassandraSessionSettings.create()
    implicit val session: CassandraSession = CassandraSessionRegistry.get(system).sessionFor(sessionSettings)
    new CassandraTestkitProduction()
  }
}
