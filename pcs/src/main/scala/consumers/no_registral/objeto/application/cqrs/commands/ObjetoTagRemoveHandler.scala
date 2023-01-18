package consumers.no_registral.objeto.application.cqrs.commands

import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.domain.ObjetoEvents
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import design_principles.actor_model.Response

import scala.util.{Success, Try}

class ObjetoTagRemoveHandler(actor: ObjetoActor) extends SyncCommandHandler[ObjetoCommands.ObjetoTagRemove] {
  override def handle(
      command: ObjetoCommands.ObjetoTagRemove
  ): Try[Response.SuccessProcessing] = {
    val sender = actor.context.sender()

    val event = ObjetoEvents.ObjetoTagAdded(command.deliveryId,
                                            command.sujetoId,
                                            command.objetoId,
                                            command.tipoObjeto,
                                            command.tag)
    actor.persistEvent(event) { () =>
      actor.state += event
      actor.persistSnapshot(event, actor.state) { () =>
        sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
      }
    }
    Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))

  }
}
