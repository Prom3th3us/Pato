package consumers.registral.domicilio_sujeto.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoCommands.DomicilioSujetoUpdateFromDto
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoEvents.DomicilioSujetoUpdatedFromDto
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoState
import consumers.registral.domicilio_sujeto.infrastructure.json.DomiciliSujetoUpdatedF
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class DomicilioSujetoUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: DomicilioSujetoUpdateFromDto)(replyTo: ActorRef[Success]) =
    Effect
      .persist[
        DomicilioSujetoUpdatedFromDto,
        DomicilioSujetoState
      ](
        DomicilioSujetoUpdatedFromDto(
          command.deliveryId,
          command.sujetoId,
          command.domicilioId,
          command.registro
        )
      )
      .thenRun(state =>
        messageProducer.produce(
          Seq(
            KafkaKeyValue(command.aggregateRoot,
                          serialization.encode(
                            DomicilioSujetoUpdatedFromDto(
                              command.deliveryId,
                              command.sujetoId,
                              command.domicilioId,
                              command.registro
                            )
                          ))
          ),
          "DomicilioSujetoUpdatedFromDto"
        )(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }

}
