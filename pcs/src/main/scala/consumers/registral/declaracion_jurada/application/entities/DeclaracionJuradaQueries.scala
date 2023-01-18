package consumers.registral.declaracion_jurada.application.entities

import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaResponses.GetDeclaracionJuradaResponse
import design_principles.actor_model.Query

sealed trait DeclaracionJuradaQueries extends Query with DeclaracionJuradaMessage

object DeclaracionJuradaQueries {

  case class GetStateDeclaracionJurada(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      declaracionJuradaId: String
  ) extends DeclaracionJuradaQueries {
    override type ReturnType = GetDeclaracionJuradaResponse
  }
}
