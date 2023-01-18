package design_principles.projection.mock

import scala.collection.mutable

import design_principles.projection.CassandraTestkit

class CassandraTestkitMock(proyectionistReaction: Any => (String, String)) extends CassandraTestkit {

  val rowsAsMap: mutable.Map[String, Map[String, String]] = mutable.Map.empty[String, Map[String, String]]
  val cassandraWrite: CassandraWriteMock = new CassandraWriteMock(rowsAsMap, proyectionistReaction)
  val cassandraRead: CassandraReadMock = new CassandraReadMock(rowsAsMap)
}
