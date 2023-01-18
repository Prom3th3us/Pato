package consumers.registral.declaracion_jurada.infrastructure.http

import java.time.LocalDateTime

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaQueries.GetStateDeclaracionJurada
import consumers.registral.declaracion_jurada.infrastructure.dependency_injection.DeclaracionJuradaActor
import consumers.registral.declaracion_jurada.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class DeclaracionJuradaStateAPI(actor: DeclaracionJuradaActor, monitoring: Monitoring)(implicit
                                                                                            system: ActorSystem[_])
    extends QueryStateAPI(monitoring) {
  import DeclaracionJuradaStateAPI._

  def getState: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        withTipoObjeto { tipoObjeto =>
          declaracionesJuradas { declaracionesJuradaId =>
            val query = GetStateDeclaracionJurada(sujetoId, objetoId, tipoObjeto, declaracionesJuradaId)
            queryState(actor, query)(
              GetDeclaracionJuradaResponseF,
              state => state.fechaUltMod == LocalDateTime.MIN
            )
          }
        }
      }
    }

  def route: Route = GET(getState)
}

object DeclaracionJuradaStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withSujeto: (String => Route) => Route = nestedRoute("sujeto") _
  def withObjeto: (String => Route) => Route = nestedRoute("objeto") _
  def withTipoObjeto: (String => Route) => Route = nestedRoute("tipo") _
  def declaracionesJuradas = path("declaraciones_juradas" / Segment)

}
