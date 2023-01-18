package consumers.registral.domicilio_sujeto.application.entities

import java.time.LocalDateTime

sealed trait DomicilioSujetoResponses
object DomicilioSujetoResponses {

  case class GetDomicilioSujetoResponse(registro: Option[DomicilioSujetoExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
