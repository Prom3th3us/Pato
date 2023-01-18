package design_principles.projection.mock

import scala.collection.mutable
import scala.concurrent.Future

import cassandra.read.CassandraRead
import org.scalatest.concurrent.ScalaFutures

class CassandraReadMock(rowsAsMap: mutable.Map[String, Map[String, String]]) extends CassandraRead with ScalaFutures {
  override def getRow(key: String): Future[Option[Map[String, String]]] =
    Future.successful(rowsAsMap.get(key))

  def getEvent(key: String): Option[String] = getRow(key).futureValue match {
    case Some(value) => value.get("event")
    case None => None
  }
}
