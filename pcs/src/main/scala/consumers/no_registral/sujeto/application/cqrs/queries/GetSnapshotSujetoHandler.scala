package consumers.no_registral.sujeto.application.cqrs.queries

import consumers.no_registral.sujeto.application.entity.SujetoQueries.{GetSnapshotSujeto, GetStateSujeto}
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import cqrs.untyped.query.QueryHandler.SyncQueryHandler

import scala.util.{Success, Try}

class GetSnapshotSujetoHandler(actor: SujetoActor) extends SyncQueryHandler[GetSnapshotSujeto] {
  override def handle(query: GetSnapshotSujeto): Try[GetSnapshotSujeto#ReturnType] = {
    val sender = actor.context.sender()

    val response =
      GetSujetoResponse(
        actor.state.saldo,
        actor.state.objetos map { case (objetoId, tipoObjeto) => s"$objetoId|$tipoObjeto" },
        actor.state.fechaUltMod,
        actor.state.registro
      )

    actor.saveSnapshot(actor.state)

    log.info(s"[${actor.persistenceId}] GetState | $response")
    sender ! response
    Success(response)
  }
}
