package consumers.registral.actividad_sujeto.application.entities

import java.time.LocalDateTime

sealed trait ActividadSujetoResponses
object ActividadSujetoResponses {

  case class GetActividadSujetoResponse(
      registro: Option[ActividadSujetoExternalDto] = None,
      fechaUltMod: LocalDateTime
  ) extends design_principles.actor_model.Response
}
