package consumers.no_registral.obligacion.application.cqrs.commands

import consumers.no_registral.obligacion.application.entities.ObligacionCommands.ObligacionRemove
import consumers.no_registral.obligacion.domain.ObligacionEvents
import consumers.no_registral.obligacion.infrastructure.dependency_injection.ObligacionActor
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import design_principles.actor_model.Response
import design_principles.actor_model.mechanism.DeliveryIdManagement

import scala.util.{Success, Try}

class ObligacionRemoveHandler(actor: ObligacionActor) extends SyncCommandHandler[ObligacionRemove] {
  override def handle(command: ObligacionRemove): Try[Response.SuccessProcessing] = {
    println(s"[PEPE] LOG")
    log.error(s"[PEPE] LOG")

    val sender = actor.context.sender()

    val event =
      ObligacionEvents.ObligacionRemoved(
        command.deliveryId,
        command.sujetoId,
        command.objetoId,
        command.tipoObjeto,
        command.obligacionId,
        command.registro,
        command.cuota
      )

    if (DeliveryIdManagement.isIdempotent(event, command, actor.state.lastDeliveryIdByEvents)) {
      log.error(s"[${actor.name} | ${actor.persistenceId}] respond idempotent because of old delivery id | $command")

      // Informs that operation has been ignored */
      //todo check if this is desirable, why? signal the sender??

      // In this case the sender is "EL OBJETO"
      sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

      Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
    } else {
      actor.persistEvent(event) { () =>
        actor.state += event

        // Propaga actualizaciones al padre (Objeto)
        actor.informRemoveToParent(command)
        actor.lastDeliveryId = command.deliveryId
        actor.deleteSnapshot() { () =>
          sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
        }
        sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
      }
      Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
    }
  }
}
/*val stateIsEmpty = state.equals(state.empty)
    //todo warning !
    val kafkaTopic = (if(stateIsEmpty) {
      "ObligacionDeletedSnapshot"
    }
    else {
      "ObligacionPersistedSnapshot"
    })*/
