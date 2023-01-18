package consumers.registral.domicilio_sujeto.application.entities

import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoResponses.GetDomicilioSujetoResponse
import design_principles.actor_model.Query

sealed trait DomicilioSujetoQueries extends Query with DomicilioSujetoMessage

object DomicilioSujetoQueries {
  case class GetStateDomicilioSujeto(
      sujetoId: String,
      domicilioId: String
  ) extends DomicilioSujetoQueries {
    override type ReturnType = GetDomicilioSujetoResponse
  }
}
