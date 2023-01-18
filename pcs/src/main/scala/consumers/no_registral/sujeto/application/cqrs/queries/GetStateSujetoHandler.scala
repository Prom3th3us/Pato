package consumers.no_registral.sujeto.application.cqrs.queries

import consumers.no_registral.sujeto.application.entity.SujetoQueries.GetStateSujeto
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import cqrs.untyped.query.QueryHandler.SyncQueryHandler

import scala.util.{Success, Try}

class GetStateSujetoHandler(actor: SujetoActor) extends SyncQueryHandler[GetStateSujeto] {
  override def handle(query: GetStateSujeto): Try[GetStateSujeto#ReturnType] = {
    val sender = actor.context.sender()

    val response =
      GetSujetoResponse(
        actor.state.saldo,
        actor.state.objetos map { case (objetoId, tipoObjeto) => s"$objetoId|$tipoObjeto" },
        actor.state.fechaUltMod,
        actor.state.registro
      )
    log.info(s"[${actor.persistenceId}] GetState | $response")
    sender ! response
    Success(response)
  }
}
