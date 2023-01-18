package consumers.registral.subasta.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.subasta.application.entities.SubastaCommands.SubastaUpdateFromDto
import consumers.registral.subasta.domain.SubastaEvents.SubastaUpdatedFromDto
import consumers.registral.subasta.domain.SubastaState
import consumers.registral.subasta.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class SubastaUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: SubastaUpdateFromDto)(replyTo: ActorRef[Success]) = {
    val event = SubastaUpdatedFromDto(
      command.deliveryId,
      command.sujetoId,
      command.objetoId,
      command.tipoObjeto,
      command.subastaId,
      command.registro
    )
    Effect
      .persist[
        SubastaUpdatedFromDto,
        SubastaState
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
                                "SubastaUpdatedFromDto")(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
  }
}
