package consumers.no_registral.sujeto.infrastructure.http

import java.time.LocalDateTime
import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.no_registral.obligacion.infrastructure.http.ObligacionStateAPI.withSujeto
import consumers.no_registral.sujeto.application.entity.SujetoQueries.{GetSnapshotSujeto, GetStateSujeto}
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import consumers.no_registral.sujeto.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import monitoring.Monitoring

import scala.concurrent.ExecutionContext
case class SujetoStateAPI(actor: ActorRef, monitoring: Monitoring)(
    implicit
    queryStateApiRequirements: QueryStateApiRequirements
) extends QueryStateAPI(monitoring) {

  implicit val system: ActorSystem = queryStateApiRequirements.system
  implicit val ec: ExecutionContext = queryStateApiRequirements.executionContext

  def developerTools: Route =
    withSujeto { sujetoId =>
      withDeveloperTools { command =>
        command match {
          case "sleep" =>
            log.info(s"[Sujeto-$sujetoId] ${Console.RED} Going to sleep. ${Console.RESET}")
            actor ! PoisonPill
        }
        complete { HttpResponse(OK) }
      }
    }

  def getState: Route =
    path("sujeto" / Segment) { sujetoId =>
      queryState[GetSujetoResponse](actor, GetStateSujeto(sujetoId))(
        GetSujetoResponseF,
        state => state.fechaUltMod == LocalDateTime.MIN
      )
    }

  def getSnapshot: Route =
    path("sujeto" / Segment / "snapshot") { sujetoId =>
      queryState[GetSujetoResponse](actor, GetSnapshotSujeto(sujetoId))(
        GetSujetoResponseF,
        state => state.fechaUltMod == LocalDateTime.MIN
      )
    }

  def route: Route = GET(getState) ~ GET(developerTools) ~ GET(getSnapshot)
  def withDeveloperTools = path("developer" / "tools" / Segment)
}
