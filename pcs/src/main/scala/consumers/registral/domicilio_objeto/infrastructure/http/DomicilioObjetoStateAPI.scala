package consumers.registral.domicilio_objeto.infrastructure.http

import java.time.LocalDateTime

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoQueries.GetStateDomicilioObjeto
import consumers.registral.domicilio_objeto.infrastructure.dependency_injection.DomicilioObjetoActor
import consumers.registral.domicilio_objeto.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class DomicilioObjetoStateAPI(actor: DomicilioObjetoActor, monitoring: Monitoring)(
    implicit
    system: akka.actor.typed.ActorSystem[_]
) extends QueryStateAPI(monitoring) {
  import DomicilioObjetoStateAPI._
  def getState: Route =
    withSujeto { sujetoId =>
      withObjeto { objetoId =>
        withTipoObjeto { tipoObjeto =>
          withDomicilioObjeto { domicilioId =>
            queryState(actor, GetStateDomicilioObjeto(sujetoId, objetoId, tipoObjeto, domicilioId))(
              GetDomicilioObjetoResponseF,
              state => state.fechaUltMod == LocalDateTime.MIN
            )
          }
        }
      }
    }

  def route: Route = GET(getState)
}

object DomicilioObjetoStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withSujeto: (String => Route) => Route = nestedRoute("sujeto") _
  def withObjeto: (String => Route) => Route = nestedRoute("objeto") _
  def withTipoObjeto: (String => Route) => Route = nestedRoute("tipo") _
  def withDomicilioObjeto = path("domicilio_objeto" / Segment)

}
