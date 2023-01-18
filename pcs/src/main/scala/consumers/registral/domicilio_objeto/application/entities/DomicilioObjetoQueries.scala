package consumers.registral.domicilio_objeto.application.entities

import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoResponses.GetDomicilioObjetoResponse
import design_principles.actor_model.Query

sealed trait DomicilioObjetoQueries extends Query with DomicilioObjetoMessage

object DomicilioObjetoQueries {
  case class GetStateDomicilioObjeto(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      domicilioId: String
  ) extends DomicilioObjetoQueries {
    override type ReturnType = GetDomicilioObjetoResponse
  }
}
