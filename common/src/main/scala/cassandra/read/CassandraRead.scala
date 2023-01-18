package cassandra.read

import scala.concurrent.Future

trait CassandraRead {
  def getRow(cql: String): Future[Option[Map[String, String]]]
}
