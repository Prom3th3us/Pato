package consumers.no_registral.objeto.application.cqrs.queries

import consumers.no_registral.objeto.application.entities.ObjetoQueries.GetStateObjeto
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import cqrs.untyped.query.QueryHandler.SyncQueryHandler

import scala.util.{Success, Try}

class GetStateObjetoHandler(actor: ObjetoActor) extends SyncQueryHandler[GetStateObjeto] {
  override def handle(query: GetStateObjeto): Try[GetStateObjeto#ReturnType] = {
    val sender = actor.context.sender()

    val response = GetObjetoResponse(
      actor.state.saldo,
      actor.state.tags,
      actor.state.obligaciones,
      actor.state.sujetos,
      actor.state.sujetoResponsable,
      actor.state.fechaUltMod,
      actor.state.registro,
      actor.state.exenciones
    )
    log.info(s"[${actor.persistenceId}] GetState | $response")
    sender ! response
    Success(response)
  }
}
