package akka.projections.cassandra

import scala.util.{Failure, Success, Try}

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.projections.cassandra.CassandraProjectionist.CassandraProjectionistRequirements
import akka.projections.{ProjectionHandler, ProjectionSettings}
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry}
import cassandra.write.{CassandraWrite, CassandraWriteProduction}
import org.slf4j.LoggerFactory

abstract class CassandraProjectionHandler[T](settings: ProjectionSettings, system: ActorSystem[_])
    extends ProjectionHandler[T](settings, system) {

  private val sessionSettings = CassandraSessionSettings.create()
  private implicit val session: CassandraSession = CassandraSessionRegistry.get(system).sessionFor(sessionSettings)

  val cassandra: CassandraWrite = new CassandraWriteProduction()

  protected val log = LoggerFactory.getLogger(this.getClass)

  def run(): Unit = {
    Try(
      CassandraProjectionist.startProjection(
        CassandraProjectionistRequirements(
          projectionSettings = settings,
          system = system,
          projectionHandler = this
        )
      )
    ) match {
      case Failure(exception) =>
        log.error(s"CassandraProjection ${settings.name} started with Failure(${exception.getMessage})")
      case Success(value) =>
        log.info(s"CassandraProjection ${settings.name} started with Success($value)")
    }
  }

  def route: Route =
    path("api" / "projection" / settings.name / "start") {
      complete {
        StatusCodes.OK
      }
    }

}

object CassandraProjectionHandler {
  case class CassandraProjectionHandlerRequirements(
      settings: ProjectionSettings,
      system: ActorSystem[_]
  )
}
