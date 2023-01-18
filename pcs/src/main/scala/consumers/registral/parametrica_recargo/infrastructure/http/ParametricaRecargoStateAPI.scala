package consumers.registral.parametrica_recargo.infrastructure.http

import java.time.LocalDateTime

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.{Directive, Route}
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoQueries.GetStateParametricaRecargo
import consumers.registral.parametrica_recargo.infrastructure.dependency_injection.ParametricaRecargoActor
import consumers.registral.parametrica_recargo.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class ParametricaRecargoStateAPI(actor: ParametricaRecargoActor, monitoring: Monitoring)(
    implicit
    system: akka.actor.typed.ActorSystem[_]
) extends QueryStateAPI(monitoring) {
  import ParametricaRecargoStateAPI._

  def getState: Route =
    withParametricaRecargo { parametricaRecargoId =>
      queryState(actor, GetStateParametricaRecargo(parametricaRecargoId))(
        GetParametricaRecargoResponseF,
        state => state.fechaUltMod == LocalDateTime.MIN
      )
    }

  def route: Route = GET(getState)
}

object ParametricaRecargoStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withParametricaRecargo: Directive[Tuple1[String]] = path("parametrica_recargo" / Segment)
}
