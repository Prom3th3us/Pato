package consumers.no_registral.objeto.domain

import java.time.LocalDateTime
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.Exencion
import ddd.{AbstractState, eventCounterMax}

case class ObjetoState(
    saldo: BigDecimal = 0,
    obligacionesSaldo: Map[String, BigDecimal] = Map.empty,
    obligaciones: Set[String] = Set.empty,
    sujetos: Set[String] = Set.empty,
    sujetoResponsable: Option[String] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN,
    registro: Option[ObjetoExternalDto] = None,
    tags: Set[String] = Set.empty,
    isResponsable: Boolean = false,
    lastDeliveryIdByEvents:  BigInt = 0,
    porcentajeResponsabilidad: BigDecimal = 0,
    exenciones: Set[Exencion] = Set.empty,
    isBaja: Boolean = false,
    isAdheridoDebito: Boolean = false,
    eventCounter:Int = 0,
    cuotas: List[Boolean] = List(false, false, false, false, false, false, false, false, false, false, false, false, false)
) extends AbstractState[ObjetoEvents] {

  override def +(event: ObjetoEvents): ObjetoState = {
    eventCounter match {
      case n if (n > (eventCounterMax)) => changeState(event).copy(
        fechaUltMod = LocalDateTime.now,
        lastDeliveryIdByEvents =  event.deliveryId,
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
      eventCounter = eventCounter + 1
    )*/
  }

  private def changeState(event: ObjetoEvents): ObjetoState =
    event match {
      case cmd: ObjetoEvents.ObjetoUpdatedCotitulares =>
        copy(
          sujetos = cmd.cotitulares,
          isBaja = false
        )
      case ObjetoEvents.ObjetoAddedExencion(deliveryId, sujetoId, objetoId, tipoObjeto, exencion) =>
        copy(
          exenciones = exenciones + exencion,
          isBaja = false
        )
      case evt: ObjetoEvents.ObjetoUpdatedFromTri =>
        copy(
          sujetoResponsable = evt.sujetoResponsable match {
            case Some(value) => Some(value)
            case None => this.sujetoResponsable
          },
          isResponsable = evt.isResponsable.getOrElse(false),
          registro = Some(evt.registro),
          sujetos = sujetos + evt.sujetoId,
          isAdheridoDebito = evt.isAdheridoDebito.getOrElse(false),
          isBaja = false
        )
      case evt: ObjetoEvents.ObjetoUpdatedFromAnt =>
        copy(
          registro = Some(evt.registro),
          sujetos = sujetos + evt.sujetoId
        )
      case evt: ObjetoEvents.ObjetoUpdatedFromObligacion =>
        val obligacionesSaldo_ = obligacionesSaldo + (evt.obligacionId -> evt.saldoObligacion)
        copy(
          saldo = obligacionesSaldo_.values.sum,
          obligaciones = obligaciones + evt.obligacionId,
          obligacionesSaldo = obligacionesSaldo_,
          sujetos = sujetos + evt.sujetoId,
          isBaja = false
        )
      case evt: ObjetoEvents.ObjetoSnapshotPersisted =>
        copy(
          saldo = evt.saldo,
          sujetos = evt.cotitulares,
          sujetoResponsable = evt.sujetoResponsable,
          obligacionesSaldo = evt.obligacionesSaldo,
          tags = evt.tags,
          isBaja = false,
          cuotas = evt.cuotas
        )

      case evt: ObjetoEvents.ObjetoTagAdded =>
        copy(tags = tags + evt.tagAdded, isBaja = false)
      case evt: ObjetoEvents.ObjetoTagRemoved =>
        copy(tags = tags - evt.tagRemoved, isBaja = false)

      case evt: ObjetoEvents.ObjetoBajaSet =>
        copy(
          sujetoResponsable = evt.sujetoResponsable match {
            case Some(value) => Some(value)
            case None => this.sujetoResponsable
          },
          isResponsable = evt.isResponsable.getOrElse(false),
          registro = Some(evt.registro),
          isBaja = true
        )

      case evt: ObjetoEvents.ObjetoRemovedObligacion =>
        val obligacionesSaldo_ = obligacionesSaldo - (evt.obligacionId)
        if(evt.cuota.isEmpty || evt.cuota.get.toInt < 0 || evt.cuota.get.toInt > 12) {

          copy(
            saldo = obligacionesSaldo_.values.sum,
            obligaciones = obligaciones - evt.obligacionId,
            obligacionesSaldo = obligacionesSaldo_
          )
        }else {
          val cuotaIndex_ = evt.cuota.get.toInt
          val cuotasPagadas_ = cuotas.updated(cuotaIndex_, true)
          copy(
            saldo = obligacionesSaldo_.values.sum,
            obligaciones = obligaciones - evt.obligacionId,
            obligacionesSaldo = obligacionesSaldo_,
            cuotas = cuotasPagadas_
          )
        }

      case evt =>
        log.warn(s"Unexpected event at ObjetoState ${evt}")
        this
    }

  def empty = ObjetoState()

}
