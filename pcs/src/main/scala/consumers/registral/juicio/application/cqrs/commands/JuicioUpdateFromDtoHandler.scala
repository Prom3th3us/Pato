package consumers.registral.juicio.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.{Effect, ReplyEffect}
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import consumers.registral.juicio.application.entities.JuicioCommands.JuicioUpdateFromDto
import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import consumers.registral.juicio.domain.JuicioState
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer
import consumers.registral.juicio.infrastructure.json._
class JuicioUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {
  def handle(
      command: JuicioUpdateFromDto
  )(replyTo: ActorRef[Success]): ReplyEffect[JuicioUpdatedFromDto, JuicioState] = {
    Effect
      .persist[
        JuicioUpdatedFromDto,
        JuicioState
      ](
        JuicioUpdatedFromDto(
          command.deliveryId,
          command.sujetoId,
          command.objetoId,
          command.tipoObjeto,
          command.juicioId,
          command.registro,
          command.detallesJuicio
        )
      )
      .thenRun(state =>
        messageProducer.produce(
          Seq(
            KafkaKeyValue(
              command.aggregateRoot,
              serialization.encode(
                JuicioUpdatedFromDto(
                  command.deliveryId,
                  command.sujetoId,
                  command.objetoId,
                  command.tipoObjeto,
                  command.juicioId,
                  command.registro,
                  command.detallesJuicio
                )
              )
            )
          ),
          "JuicioUpdatedFromDto"
        )(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
  }
}
