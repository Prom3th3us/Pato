package cassandra.mechanism

import akka.Done

import scala.concurrent.{ExecutionContext, Future}
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import cassandra.{CassandraTypesAdapter, CqlSessionSingleton}
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement}
import ddd.ReadSideProjection
import design_principles.actor_model.Event

trait UpdateReadSideProjection[E <: Event] extends ReadSideProjection[E] {

  val session = CqlSessionSingleton.session

  final protected def boundStatement: BoundStatement =
    session.prepare(statement) bind (binds: _*)

  def updateReadside()(implicit ec: ExecutionContext): Future[Done] = {
    import scala.jdk.FutureConverters._
    session.executeAsync(boundStatement).asScala.map { _ =>
      akka.Done
    }
  }

  def event: E

  def collectionName: String

  def bindings: List[(String, Any)]

  def keys: List[(String, Any)]

  final private def sets(b: List[(String, Object)]) =
    b.map(_._1)
      .map { k =>
        s"$k=?"
      }
      .mkString(",\n")

  final private def setsKeys(b: List[(String, Object)]) =
    b.map(_._1)
      .map { k =>
        s"$k=?"
      }
      .mkString(" AND ")

  final private def statement: String = {
    s"""UPDATE $collectionName
       | SET ${sets(curatedBindings)}
       | WHERE ${setsKeys(curatedKeys)}
      """.stripMargin
  }

  final private def curatedBindings: List[(String, Object)] = CassandraTypesAdapter.curateKeyValueList(bindings)
  final private def curatedKeys: List[(String, Object)] = CassandraTypesAdapter.curateKeyValueList(keys)

  def binds: List[Object] = (curatedBindings ++ curatedKeys).map(_._2)

}
