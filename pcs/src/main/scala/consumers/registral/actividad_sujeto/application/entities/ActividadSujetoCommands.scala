package consumers.registral.actividad_sujeto.application.entities

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto.ActividadSujeto
import design_principles.actor_model.Command

trait ActividadSujetoCommands extends Command with ActividadSujetoMessage

object ActividadSujetoCommands {

  case class ActividadSujetoUpdateFromDto(
      sujetoId: String,
      actividadSujetoId: String,
      deliveryId: BigInt,
      registro: ActividadSujeto
  ) extends ActividadSujetoCommands

}
