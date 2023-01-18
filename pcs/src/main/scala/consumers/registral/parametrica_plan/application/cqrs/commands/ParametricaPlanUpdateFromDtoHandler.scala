package consumers.registral.parametrica_plan.application.cqrs.commands

import akka.actor.Status.Success
import akka.actor.typed.ActorRef
import akka.persistence.typed.scaladsl.Effect
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanCommands.ParametricaPlanUpdateFromDto
import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents.ParametricaPlanUpdatedFromDto
import consumers.registral.parametrica_plan.domain.{ParametricaPlanEvents, ParametricaPlanState}
import consumers.registral.parametrica_plan.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class ParametricaPlanUpdateFromDtoHandler(implicit messageProducer: MessageProducer) {

  def handle(command: ParametricaPlanUpdateFromDto)(replyTo: ActorRef[Success]) = {
    val event = ParametricaPlanEvents.ParametricaPlanUpdatedFromDto(
      command.deliveryId,
      bppRdlId = command.registro.BPP_RDL_ID,
      bppFpmId = command.registro.BPP_FPM_ID,
      bppCantMaxCuotas = command.registro.BPP_CANT_MAX_CUOTAS,
      bppCantMinCuotas = command.registro.BPP_CANT_MIN_CUOTAS,
      bppDiasVtoCuotas = command.registro.BPP_DIAS_VTO_CUOTAS,
      bppFechaDesdeDeuda = command.registro.BPP_FECHA_DESDE_DEUDA,
      bppFechaFin = command.registro.BPP_FECHA_FIN,
      bppFechaHastaDeuda = command.registro.BPP_FECHA_HASTA_DEUDA,
      bppFechaInicio = command.registro.BPP_FECHA_INICIO,
      bppFpmDescripcion = command.registro.BPP_FPM_DESCRIPCION,
      bppIndiceIntFinanc = command.registro.BPP_INDICE_INT_FINANC,
      bppIndiceIntPunit = command.registro.BPP_INDICE_INT_PUNIT,
      bppIndiceIntResar = command.registro.BPP_INDICE_INT_RESAR,
      bppMontoMaxDeuda = command.registro.BPP_MONTO_MAX_DEUDA,
      bppMontoMinAnticipo = command.registro.BPP_MONTO_MIN_ANTICIPO,
      bppMontoMinCuota = command.registro.BPP_MONTO_MIN_CUOTA,
      bppMontoMinDeuda = command.registro.BPP_MONTO_MIN_DEUDA,
      bppPorcentajeAnticipo = command.registro.BPP_PORCENTAJE_ANTICIPO,
      registro = command.registro
    )
    Effect
      .persist[
        ParametricaPlanUpdatedFromDto,
        ParametricaPlanState
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
                                "ParametricaPlanUpdatedFromDto")(_ => ())
      )
      .thenReply(replyTo) { state =>
        Success(Response.SuccessProcessing(command.aggregateRoot, command.deliveryId))
      }
  }
}
