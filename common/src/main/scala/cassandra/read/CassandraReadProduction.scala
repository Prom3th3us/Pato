package cassandra.read

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSource}
import akka.stream.scaladsl.Sink
import com.datastax.oss.driver.api.core.cql.{Row, SimpleStatement}
import utils.implicits.RowT._

class CassandraReadProduction(implicit session: CassandraSession, system: ActorSystem, ec: ExecutionContext)
    extends CassandraRead {

  def cqlQuerySingleResult(cqlQuery: String): Future[Option[Row]] = {
    val stmt = SimpleStatement.newInstance(cqlQuery).setPageSize(20)
    CassandraSource(stmt)
      .runWith(Sink.seq)
      .map {
        case sequence if sequence.isEmpty =>
          None
        case sequence =>
          Some(sequence.last)
      }(system.dispatcher)
  }

  override def getRow(cql: String): Future[Option[Map[String, String]]] =
    cqlQuerySingleResult(cql).map { maybeRow =>
      maybeRow.map { row: Row =>
        row.toMap
      }
    }
}
