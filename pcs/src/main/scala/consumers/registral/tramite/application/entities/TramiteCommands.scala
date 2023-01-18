package consumers.registral.tramite.application.entities

import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import design_principles.actor_model.Command

sealed trait TramiteCommands extends Command with TramiteMessage

object TramiteCommands {
  case class TramiteUpdateFromDto(
      sujetoId: String,
      tramiteId: String,
      deliveryId: BigInt,
      registro: Tramite
  ) extends TramiteCommands
}
