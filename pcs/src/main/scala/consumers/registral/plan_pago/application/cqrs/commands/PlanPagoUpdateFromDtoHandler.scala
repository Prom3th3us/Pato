package consumers.registral.plan_pago.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.plan_pago.application.entities.PlanPagoCommands.PlanPagoUpdateFromDto
import consumers.registral.plan_pago.domain.PlanPagoEvents.PlanPagoUpdatedFromDto
import consumers.registral.plan_pago.domain.PlanPagoState
import consumers.registral.plan_pago.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class PlanPagoUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: PlanPagoUpdateFromDto)(replyTo: ActorRef[Success]) = {
    val event = PlanPagoUpdatedFromDto(
      command.deliveryId,
      command.sujetoId,
      command.objetoId,
      command.tipoObjeto,
      command.planPagoId,
      command.registro
    )
    Effect
      .persist[
        PlanPagoUpdatedFromDto,
        PlanPagoState
      ](
        event
      )
      .thenRun(state =>
        messageProducer.produce(Seq(
                                  KafkaKeyValue(command.aggregateRoot,
                                                serialization.encode(
                                                  event
                                                ))
                                ),
                                "PlanPagoUpdatedFromDto")(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
  }
}
