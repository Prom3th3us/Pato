package consumers.no_registral.objeto.application.cqrs.commands

import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import consumers.no_registral.obligacion.domain.ObligacionEvents
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import design_principles.actor_model.Response

import scala.util.{Success, Try}

class ObjetoSnapshotHandler(actor: ObjetoActor) extends SyncCommandHandler[ObjetoCommands.ObjetoSnapshot] {
  override def handle(
      command: ObjetoCommands.ObjetoSnapshot
  ): Try[Response.SuccessProcessing] = {
    val event = ObjetoSnapshotPersisted(
      command.deliveryId,
      command.sujetoId,
      command.objetoId,
      command.objetoId2,
      command.tipoObjeto,
      command.saldo,
      command.cotitulares,
      command.tags,
      command.sujetoResponsable,
      actor.state.porcentajeResponsabilidad,
      actor.state.registro,
      command.obligacionesSaldo,
      actor.state.cuotas,
      operacion = ObligacionEvents.operaciones.get("Upsert").get,
      command.idExterno
    )
    val consolidatedState = actor.state + event
    val sender = actor.context.sender()
    actor.persistSnapshot(event, consolidatedState) { () =>
      actor.state = consolidatedState
      actor.informParent(command, actor.state)
      sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
    }
    Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
  }
}
