package consumers.no_registral.objeto.application.cqrs.commands

import design_principles.actor_model.mechanism.DeliveryIdManagement._

import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.domain.ObjetoEvents
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import design_principles.actor_model.Response

import scala.util.{Success, Try}

class ObjetoUpdateFromAntHandler(actor: ObjetoActor) extends SyncCommandHandler[ObjetoCommands.ObjetoUpdateFromAnt] {
  override def handle(
      command: ObjetoCommands.ObjetoUpdateFromAnt
  ): Try[Response.SuccessProcessing] = {
    val sender = actor.context.sender()

    val event = ObjetoEvents.ObjetoUpdatedFromAnt(
      command.deliveryId,
      command.sujetoId,
      command.objetoId,
      command.tipoObjeto,
      command.registro
    )
    if (isIdempotent(event, command, actor.state.lastDeliveryIdByEvents)) {
      log.warn(s"[${actor.name} | ${actor.persistenceId}] respond idempotent because of old delivery id | $command")
      sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
    } else {


      actor.persistEvent(event) { () =>
        actor.state += event
        actor.informParent(command, actor.state)
        actor.persistSnapshot(event, actor.state) { () =>
          sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
        }
      }
    }
    Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
  }
}
