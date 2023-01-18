package consumers.no_registral.objeto.application.cqrs.commands

import scala.util.{Success, Try}
import consumers.no_registral.objeto.application.entities.ObjetoCommands
import consumers.no_registral.objeto.domain.ObjetoEvents
import consumers.no_registral.objeto.infrastructure.dependency_injection.ObjetoActor
import consumers.no_registral.obligacion.application.entities.ObligacionCommands
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto
import cqrs.untyped.command.CommandHandler.SyncCommandHandler
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer
import consumers.no_registral.objeto.infrastructure.json._
class ObjetoAddExencionHandler(actor: ObjetoActor)(implicit messageProducer: MessageProducer)
    extends SyncCommandHandler[ObjetoCommands.ObjetoAddExencion] {
  override def handle(
      command: ObjetoCommands.ObjetoAddExencion
  ): Try[Response.SuccessProcessing] = {
    val replyTo = actor.sender()
    val event = ObjetoEvents.ObjetoAddedExencion(
      command.deliveryId,
      command.sujetoId,
      command.objetoId,
      command.tipoObjeto,
      command.exencion
    )
    val sender = actor.context.sender()
    val documentName = utils.Inference.getSimpleName(event.getClass.getName)
    val lastDeliveryId = actor.state.lastDeliveryIdByEvents
    if (event.deliveryId <= lastDeliveryId) {
      log.warn(s"[${actor.persistenceId}] respond idempotent because of old delivery id | $command")
      sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
    } else {
      actor.persistEvent(event) { () =>
        actor.state += event
        actor.state.obligaciones.foreach { obligacionId =>
          val obligacion = actor.obligaciones((command.sujetoId, command.objetoId, command.tipoObjeto, obligacionId))
          obligacion ! ObligacionCommands.ObligacionUpdateExencion(command.deliveryId,
                                                                   command.sujetoId,
                                                                   command.objetoId,
                                                                   command.tipoObjeto,
                                                                   obligacionId.split("-").last,
                                                                   command.exencion)
        }
        actor.informParent(command, actor.state)

        messageProducer.produce(
          Seq(
            KafkaKeyValue(
              command.aggregateRoot,
              serialization.encode(
                event
              )
            )
          ),
          "ObjetoAddedExencion"
        )(_ => ())

        sender ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
      }
    }
    Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
  }

}
