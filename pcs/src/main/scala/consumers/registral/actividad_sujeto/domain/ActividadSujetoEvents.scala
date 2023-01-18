package consumers.registral.actividad_sujeto.domain
import consumers.registral.actividad_sujeto.application.entities.{ActividadSujetoExternalDto, ActividadSujetoMessage}
import design_principles.actor_model.Event

sealed trait ActividadSujetoEvents extends Event with ActividadSujetoMessage {
  def sujetoId: String
  def actividadSujetoId: String
}

object ActividadSujetoEvents {

  case class ActividadSujetoUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      actividadSujetoId: String,
      registro: ActividadSujetoExternalDto
  ) extends ActividadSujetoEvents
}
