package consumers.registral.calendario.infrastructure.http

import java.time.LocalDateTime

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.registral.calendario.application.entities.CalendarioQueries.GetStateCalendario
import consumers.registral.calendario.infrastructure.dependency_injection.CalendarioActor
import consumers.registral.calendario.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class CalendarioStateAPI(actor: CalendarioActor, monitoring: Monitoring)(
    implicit
    system: akka.actor.typed.ActorSystem[_]
) extends QueryStateAPI(monitoring) {
  import CalendarioStateAPI._
  def getState: Route =
    withCalendario { calendarioId =>
      queryState(actor, GetStateCalendario(calendarioId))(
        GetCalendarioResponseF,
        state => state.fechaUltMod == LocalDateTime.MIN
      )
    }

  def route: Route = GET(getState)
}

object CalendarioStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withCalendario = path("calendario" / Segment)

}
