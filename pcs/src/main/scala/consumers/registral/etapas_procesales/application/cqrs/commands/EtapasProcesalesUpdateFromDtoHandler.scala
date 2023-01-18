package consumers.registral.etapas_procesales.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesCommands.EtapasProcesalesUpdateFromDto
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import consumers.registral.etapas_procesales.domain.EtapasProcesalesState
import consumers.registral.etapas_procesales.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class EtapasProcesalesUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: EtapasProcesalesUpdateFromDto)(replyTo: ActorRef[Success]) =
    Effect
      .persist[
        EtapasProcesalesUpdatedFromDto,
        EtapasProcesalesState
      ](
        EtapasProcesalesUpdatedFromDto(
          command.deliveryId,
          command.juicioId,
          command.etapaId,
          command.registro
        )
      )
      .thenRun(state =>
        messageProducer.produce(
          Seq(
            KafkaKeyValue(command.aggregateRoot,
                          serialization.encode(
                            EtapasProcesalesUpdatedFromDto(
                              command.deliveryId,
                              command.juicioId,
                              command.etapaId,
                              command.registro
                            )
                          ))
          ),
          "EtapasProcesalesUpdatedFromDto"
        )(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
}
