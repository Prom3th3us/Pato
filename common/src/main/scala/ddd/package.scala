import akka.Done

import scala.concurrent.{ExecutionContext, Future}
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import com.datastax.oss.driver.api.core.cql.BoundStatement
import design_principles.actor_model.Event
import org.slf4j.{Logger, LoggerFactory}

import scala.util.Try

package object ddd {

  trait Deliverable {
    def deliveryId: BigInt
  }

  trait AggregateRootExtractor[Ids] {
    def extractIds(givenAggregateRoot: String): Ids
  }

  trait AbstractState[Event] {
    def +(e: Event): AbstractState[Event]
    val log: Logger = LoggerFactory.getLogger(this.getClass)
  }

  trait ExternalDto

  trait ReadSideProjection[E <: Event] {
    def event: E
    def updateReadside()(implicit ec: ExecutionContext): Future[Done]
    //def prepareStatement(session: CassandraSession)(implicit ec: ExecutionContext): Future[BoundStatement]
  }
  val eventCounterMax: Int = Try(System.getenv("EVENT_COUNTER_MAX").toInt).getOrElse(9)

}
