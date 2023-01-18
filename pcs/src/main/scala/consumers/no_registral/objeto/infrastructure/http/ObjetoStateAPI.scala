package consumers.no_registral.objeto.infrastructure.http

import java.time.LocalDateTime
import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.no_registral.objeto.application.entities.ObjetoQueries.{
  GetSnapshotObjeto,
  GetStateExencion,
  GetStateObjeto
}
import consumers.no_registral.objeto.application.entities.ObjetoResponses.{GetExencionResponse, GetObjetoResponse}
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import monitoring.Monitoring

import scala.concurrent.ExecutionContext

case class ObjetoStateAPI(actor: ActorRef, monitoring: Monitoring)(
    implicit
    queryStateApiRequirements: QueryStateApiRequirements
) extends QueryStateAPI(monitoring) {

  implicit val system: ActorSystem = queryStateApiRequirements.system
  implicit val ec: ExecutionContext = queryStateApiRequirements.executionContext
  import ObjetoStateAPI._

  def developerTools: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        withTipoObjeto { tipoObjeto =>
          path("developer" / "tools" / Segment) { command =>
            command match {
              case "sleep" =>
                log.info(
                  s"[Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto] ${Console.RED} Going to sleep. ${Console.RESET}"
                )
                actor ! PoisonPill
            }

            complete { HttpResponse(OK) }
          }
        }
      }
    }

  def getState: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        path("tipo" / Segment) { tipoObjeto =>
          queryState[GetObjetoResponse](actorRef = actor, GetStateObjeto(sujetoId, objetoId, tipoObjeto))(
            GetObjetoResponseF,
            _.fechaUltMod == LocalDateTime.MIN
          )
        }
      }
    }

  def getExencion: Route =
    withSujeto { sujeto =>
      withObjeto { objeto =>
        withTipoObjeto { tipoObjeto =>
          path("exencion" / Segment) { id =>
            queryState[GetExencionResponse](
              actorRef = actor,
              GetStateExencion(sujeto, objeto, tipoObjeto, id)
            )(
              GetExencionResponseF,
              _.exencion.isEmpty
            )
          }
        }
      }
    }

  def getSnapshot: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        path("tipo" / Segment / "snapshot") { tipoObjeto =>
          queryState[GetObjetoResponse](actorRef = actor, GetSnapshotObjeto(sujetoId, objetoId, tipoObjeto))(
            GetObjetoResponseF,
            _.fechaUltMod == LocalDateTime.MIN
          )
        }
      }
    }

  def route: Route = GET(getState) ~ GET(getExencion) ~ GET(getSnapshot) ~ POST(developerTools)

}

object ObjetoStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withSujeto: (String => Route) => Route = nestedRoute("sujeto") _
  def withObjeto: (String => Route) => Route = nestedRoute("objeto") _
  def withTipoObjeto: (String => Route) => Route = nestedRoute("tipo") _

}
