package consumers.no_registral.sujeto.application.cqrs.commands

import design_principles.actor_model.mechanism.DeliveryIdManagement._
import akka.Done
import consumers.no_registral.sujeto.application.entity.SujetoCommands.SujetoUpdateFromTri
import consumers.no_registral.sujeto.domain.SujetoEvents.SujetoUpdatedFromTri
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import ddd.eventCounterMax
import design_principles.actor_model.Response

import scala.util.{Success, Try}

class SujetoUpdateFromTriHandler(actor: SujetoActor) extends SyncCommandHandler[SujetoUpdateFromTri] {
  override def handle(command: SujetoUpdateFromTri): Try[Response.SuccessProcessing] = {
    val sender = actor.context.sender()

    val event = SujetoUpdatedFromTri(command.deliveryId, command.sujetoId, command.registro)

    if (isIdempotent(event, command, actor.state.lastDeliveryIdByEvents)) {
      log.warn(s"[${actor.name} | ${actor.persistenceId}] respond idempotent because of old delivery id | $command")
      sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
    } else {

      actor.persistEvent(event) { () =>
        actor.state += event
        if (actor.state.eventCounter == eventCounterMax) {
          actor.saveSnapshot(actor.state.copy(eventCounter = 0))
        }
        actor.persistSnapshot() { _ =>
          sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

        }
      }
    }
    Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
  }
}
