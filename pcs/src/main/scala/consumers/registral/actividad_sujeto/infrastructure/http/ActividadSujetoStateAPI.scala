package consumers.registral.actividad_sujeto.infrastructure.http

import java.time.LocalDateTime

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.{Directive, Route}
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoQueries.GetStateActividadSujeto
import consumers.registral.actividad_sujeto.infrastructure.dependency_injection.ActividadSujetoActor
import consumers.registral.actividad_sujeto.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class ActividadSujetoStateAPI(actor: ActividadSujetoActor, monitoring: Monitoring)(implicit system: ActorSystem[_])
    extends QueryStateAPI(monitoring) {
  import ActividadSujetoStateAPI._
  def getState: Route =
    withSujeto { sujetoId =>
      withActividadSujeto { actividadId =>
        val query = GetStateActividadSujeto(sujetoId, actividadId)
        queryState(actor, query)(
          GetActividadSujetoResponseF,
          state => state.fechaUltMod == LocalDateTime.MIN
        )

      }
    }

  def route: Route = GET(getState)
}

object ActividadSujetoStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withSujeto: (String => Route) => Route = nestedRoute("sujeto") _
  def withActividadSujeto: Directive[Tuple1[String]] = path("actividades" / Segment)
}
