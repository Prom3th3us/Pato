package consumers.no_registral.cotitularidad.infrastructure.http

import java.time.LocalDateTime

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.no_registral.cotitularidad.application.entities.CotitularidadQueries.GetCotitulares
import consumers.no_registral.cotitularidad.application.entities.CotitularidadResponses.GetCotitularesResponse
import consumers.no_registral.cotitularidad.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import monitoring.Monitoring

import scala.concurrent.ExecutionContext

case class CotitularidadStateAPI(actor: ActorRef, monitoring: Monitoring)(
    implicit
    queryStateApiRequirements: QueryStateApiRequirements
) extends QueryStateAPI(monitoring) {
  implicit val system: ActorSystem = queryStateApiRequirements.system
  implicit val ec: ExecutionContext = queryStateApiRequirements.executionContext

  import CotitularidadStateAPI._
  def getState: Route =
    withObjeto { objetoId =>
      path("tipo" / Segment) { tipoObjeto =>
        queryState[GetCotitularesResponse](actorRef = actor, GetCotitulares(objetoId, tipoObjeto))(
          GetCotitularesResponseF,
          _.fechaUltMod == LocalDateTime.MIN
        )
      }

    }

  def route: Route = GET(getState)
}

object CotitularidadStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withObjeto: (String => Route) => Route = nestedRoute("objeto") _
  def withTipoObjeto: (String => Route) => Route = nestedRoute("tipo") _

}
