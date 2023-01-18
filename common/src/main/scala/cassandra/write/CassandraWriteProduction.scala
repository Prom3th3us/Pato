package cassandra.write

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import akka.Done
import cassandra.CqlSessionSingleton
import ddd.ReadSideProjection
import design_principles.actor_model.Event
import org.slf4j.LoggerFactory
import scala.jdk.FutureConverters.CompletionStageOps

class CassandraWriteProduction extends CassandraWrite {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val session = CqlSessionSingleton.session

  def writeState[E <: Event](
      state: ReadSideProjection[E]
  )(implicit ec: ExecutionContext): Future[Done] = {
    val result = for {
      done <- state.updateReadside()
    } yield done
    result.onComplete {
      case Failure(throwable) =>
        logger.warn("Cassandra failed with {} due to {}", state.event.toString, throwable.toString)
      case Success(value) =>
        logger.debug("Cassandra succeeded with value {}", value.toString)
    }
    result
  }

  override def cql(cql: String)(implicit ec: ExecutionContext): Future[Done] =
    session.executeAsync(cql).asScala.map { _ =>
      akka.Done
    }
}
