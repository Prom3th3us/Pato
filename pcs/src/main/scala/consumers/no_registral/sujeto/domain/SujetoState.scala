package consumers.no_registral.sujeto.domain

import java.time.LocalDateTime
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto
import ddd.{AbstractState, eventCounterMax}

import scala.util.Try

final case class SujetoState(
    saldo: BigDecimal = 0,
    saldoObjetos: Map[String, BigDecimal] = Map.empty,
    saldoObligaciones: Map[String, BigDecimal] = Map.empty,
    objetos: Set[(String, String)] = Set.empty,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN,
    registro: Option[SujetoExternalDto] = None,
    lastDeliveryIdByEvents:  BigInt = 0,
    eventCounter:Int = 0,
    lastInternalDeliveryId:BigInt = 0
) extends AbstractState[SujetoEvents] {
  def +(event: SujetoEvents): SujetoState = {
    eventCounter match {
      case n if (n > (eventCounterMax)) => changeState(event).copy(
        fechaUltMod = LocalDateTime.now,
        lastDeliveryIdByEvents = event.deliveryId,
        eventCounter = 0
      )
      case n => changeState(event).copy(
        fechaUltMod = LocalDateTime.now,
        lastDeliveryIdByEvents = event.deliveryId,
        eventCounter = n + 1
      )
    }
    /*changeState(event).copy(
      fechaUltMod = LocalDateTime.now,
      lastDeliveryIdByEvents = lastDeliveryIdByEvents + ((event.getClass.getSimpleName, event.deliveryId)),
      eventCounter = c
    )*/
  }


  private def changeState(event: SujetoEvents): SujetoState =
    event match {
      case SujetoEvents.SujetoUpdatedFromTri(_, _, registro) =>
        copy(
          registro = Some(registro)
        )
      case SujetoEvents.SujetoUpdatedFromAnt(_, _, registro) =>
        copy(
          registro = Some(registro)
        )
      case SujetoEvents.SujetoUpdatedFromObjeto(deliveryId, _, objetoId, tipoObjeto, saldoObjeto, _saldoObligaciones) =>
        val objetoKey = s"$objetoId|$tipoObjeto"
        val _saldoObjetos = saldoObjetos + (objetoKey -> saldoObjeto)
        copy(
          objetos = objetos + ((objetoId, tipoObjeto)),
          saldoObjetos = _saldoObjetos,
          saldo = _saldoObjetos.values.sum,
          saldoObligaciones = saldoObligaciones + (objetoKey -> _saldoObligaciones),
          lastInternalDeliveryId = deliveryId
        )
      case SujetoEvents.SujetoBajaFromObjetoSet(deliveryId, _, objetoId, tipoObjeto) =>
        val objetoKey = s"$objetoId|$tipoObjeto"
        val _saldoObjetos = saldoObjetos - objetoKey
        copy(
          objetos = objetos - ((objetoId, tipoObjeto)),
          saldoObjetos = _saldoObjetos,
          saldo = _saldoObjetos.values.sum,
          saldoObligaciones = saldoObligaciones - objetoKey,
          lastInternalDeliveryId = deliveryId
        )
      case evt: SujetoEvents.SujetoSnapshotPersisted =>
        copy(
          saldo = evt.saldo
        )
      case _ => this
    }
}
