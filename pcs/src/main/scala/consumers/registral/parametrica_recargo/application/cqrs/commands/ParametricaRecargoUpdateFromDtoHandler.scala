package consumers.registral.parametrica_recargo.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto
import consumers.registral.parametrica_recargo.domain.{ParametricaRecargoEvents, ParametricaRecargoState}
import consumers.registral.parametrica_recargo.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class ParametricaRecargoUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: ParametricaRecargoUpdateFromDto)(replyTo: ActorRef[Success]) = {

    val registro = command.registro
    val event = ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto(
      command.deliveryId,
      bprIndice = registro.BPR_INDICE,
      bprTipoIndice = registro.BPR_TIPO_INDICE,
      bprFechaDesde = registro.BPR_FECHA_DESDE,
      bprPeriodo = registro.BPR_PERIODO,
      bprConcepto = registro.BPR_CONCEPTO,
      bprImpuesto = registro.BPR_IMPUESTO,
      registro
    )
    Effect
      .persist[
        ParametricaRecargoUpdatedFromDto,
        ParametricaRecargoState
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
                                "ParametricaRecargoUpdatedFromDto")(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
  }

}
