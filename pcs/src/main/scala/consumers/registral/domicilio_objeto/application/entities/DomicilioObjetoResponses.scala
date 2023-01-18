package consumers.registral.domicilio_objeto.application.entities

import java.time.LocalDateTime

sealed trait DomicilioObjetoResponses
object DomicilioObjetoResponses {

  case class GetDomicilioObjetoResponse(registro: Option[DomicilioObjetoExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
