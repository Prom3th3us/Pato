package consumers.no_registral.objeto.application.cqrs.queries

import consumers.no_registral.objeto.application.entities.ObjetoQueries.GetStateExencion
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetExencionResponse
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import cqrs.untyped.query.QueryHandler.SyncQueryHandler

import scala.util.{Success, Try}

class GetStateExencionHandler(actor: ObjetoActor) extends SyncQueryHandler[GetStateExencion] {
  override def handle(query: GetStateExencion): Try[GetStateExencion#ReturnType] = {
    val sender = actor.context.sender()

    val response =
      GetExencionResponse(
        actor.state.exenciones.find(_.BEX_EXE_ID == query.exencion)
      )
    log.info(s"[${actor.persistenceId}] GetState | $response")
    sender ! response
    Success(response)
  }
}
