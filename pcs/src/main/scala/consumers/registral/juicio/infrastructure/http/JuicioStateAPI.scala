package consumers.registral.juicio.infrastructure.http

import java.time.LocalDateTime

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.registral.juicio.application.entities.JuicioQueries.GetStateJuicio
import consumers.registral.juicio.infrastructure.dependency_injection.JuicioActor
import consumers.registral.juicio.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class JuicioStateAPI(actor: JuicioActor, monitoring: Monitoring)(implicit system: akka.actor.typed.ActorSystem[_])
    extends QueryStateAPI(monitoring) {
  import JuicioStateAPI._

  def getState: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        withTipoObjeto { tipoObjeto =>
          withJuicio { juicioId =>
            queryState(actor, GetStateJuicio(sujetoId, objetoId, tipoObjeto, juicioId))(
              GetJuicioResponseF,
              state => state.fechaUltMod == LocalDateTime.MIN
            )
          }
        }
      }
    }

  def route: Route = GET(getState)
}

object JuicioStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withSujeto: (String => Route) => Route = nestedRoute("sujeto") _
  def withObjeto: (String => Route) => Route = nestedRoute("objeto") _
  def withTipoObjeto: (String => Route) => Route = nestedRoute("tipo") _
  def withJuicio = path("juicio" / Segment)

}
