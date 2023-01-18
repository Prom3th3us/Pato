package consumers.registral.tramite.domain

import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import consumers.registral.tramite.application.entities.TramiteMessage
import design_principles.actor_model.Event

sealed trait TramiteEvents extends Event with TramiteMessage {
  def sujetoId: String
  def tramiteId: String
}

object TramiteEvents {
  case class TramiteUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      tramiteId: String,
      registro: Tramite
  ) extends TramiteEvents
}
