package consumers.no_registral.obligacion.domain

import java.time.LocalDateTime
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.DetallesObligacion
import ddd._

import scala.util.Try

case class ObligacionState(
    saldo: BigDecimal = 0,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN,
    exenta: Boolean = false,
    porcentajeExencion: Option[BigDecimal] = None,
    registro: Option[ObligacionExternalDto] = None,
    lastDeliveryIdByEvents: BigInt = 0,
    detallesObligacion: Seq[DetallesObligacion] = Seq.empty,
    juicioId: Option[BigInt] = None,
    isAdheridoDebito: Boolean = false,
    eventCounter:Int = 0,
    idExterno: Option[String] = None
) extends AbstractState[ObligacionEvents] {

  //val eventCounterMax = Try(System.getenv("EVENT_COUNTER_MAX")).getOrElse(9)

  override def +(event: ObligacionEvents): ObligacionState = {
    eventCounter match {
      case n if (n > (eventCounterMax)) => changeState(event).copy(fechaUltMod = LocalDateTime.now, eventCounter = 0)
      case n => changeState(event).copy(fechaUltMod = LocalDateTime.now, eventCounter = n + 1)
    }
  }

  private def changeState(event: ObligacionEvents): ObligacionState =
    event match {
      case e: ObligacionEvents.ObligacionAddedExencion =>
        copy(
          exenta = true,
          porcentajeExencion = e.exencion.BEX_PORCENTAJE,
          lastDeliveryIdByEvents =  e.deliveryId
        )
      case e: ObligacionEvents.ObligacionRemoved =>
        copy(saldo = 0,
          registro = Some(e.registro),
          lastDeliveryIdByEvents =  e.registro.EV_ID)
      case e: ObligacionEvents.ObligacionUpdatedFromDto =>
        copy(
          saldo = e.registro.BOB_SALDO,
          registro = Some(e.registro),
          detallesObligacion = e.detallesObligacion,
          juicioId = e.registro.BOB_JUI_ID,
          lastDeliveryIdByEvents =  e.deliveryId,
          isAdheridoDebito = e.isAdheridoDebito.getOrElse(false),
          idExterno = e.registro.SOJ_ID_EXTERNO
        )
      case _ => this
    }

  //def empty = ObligacionState()


}
