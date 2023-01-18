package infrastructure.cassandra

import scala.concurrent.{ExecutionContext, Future}
import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry, CassandraSource}
import akka.stream.scaladsl.Sink
import com.datastax.oss.driver.api.core.cql.{Row, SimpleStatement}

// @TODO session should be injected and managed (closed) from outside
class CassandraClient(implicit system: ActorSystem, executionContext: ExecutionContext) {

  val sessionSettings = CassandraSessionSettings()
  implicit val cassandraSession: CassandraSession =
    CassandraSessionRegistry.get(system).sessionFor(sessionSettings)

  def getEvents(persistenceId: String = "Sujeto"): Future[Seq[String]] = {
    val cqlQuery = s"SELECT blobAsText(event) AS event FROM akka.messages"
    val stmt = SimpleStatement.newInstance(cqlQuery).setPageSize(20)
    CassandraSource(stmt).runWith(Sink.seq).map { table =>
      table map { row =>
        row.getString("event")
      }
    }
  }

  def executeDDL(cql: String): Future[Done] =
    cassandraSession.executeDDL(cql)

  def cqlQuery(cqlQuery: String): Future[Seq[Row]] = {
    val stmt = SimpleStatement.newInstance(cqlQuery).setPageSize(20)
    CassandraSource(stmt).runWith(Sink.seq)
  }

  def cqlQuerySingleResult(cqlQuery: String): Future[Option[Row]] = {
    val stmt = SimpleStatement.newInstance(cqlQuery).setPageSize(20)
    CassandraSource(stmt).runWith(Sink.seq).map {
      case sequence if sequence.isEmpty =>
        None
      case sequence =>
        Some(sequence.last)
    }
  }

  def close(): Unit = {
    cassandraSession.close(system.dispatcher)
  }
}
