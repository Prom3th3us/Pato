package consumers.registral.etapas_procesales.infrastructure.http

import java.time.LocalDateTime

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.{Directive, Route}
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesQueries.GetStateEtapasProcesales
import consumers.registral.etapas_procesales.infrastructure.dependency_injection.EtapasProcesalesActor
import consumers.registral.etapas_procesales.infrastructure.json._
import design_principles.actor_model.mechanism.QueryStateAPI
import monitoring.Monitoring

case class EtapasProcesalesStateAPI(actor: EtapasProcesalesActor, monitoring: Monitoring)(implicit
                                                                                          system: ActorSystem[_])
    extends QueryStateAPI(monitoring) {
  import EtapasProcesalesStateAPI._

  def getState: Route =
    withJuicio { juicioId =>
      withEtapa { etapaId =>
        queryState(actor, GetStateEtapasProcesales(juicioId, etapaId))(
          GetEtapasProcesalesResponseF,
          state => state.fechaUltMod == LocalDateTime.MIN
        )
      }
    }

  def route: Route = GET(getState)
}

object EtapasProcesalesStateAPI {
  def nestedRoute(name: String)(andThen: String => Route): Route = pathPrefix(name / Segment)(andThen)
  def withJuicio: (String => Route) => Route = nestedRoute("juicio") _
  def withEtapa: Directive[Tuple1[String]] = path("etapa" / Segment)
}
