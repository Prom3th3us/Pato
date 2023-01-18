package consumers.registral.juicio.application.entities

import consumers.registral.juicio.application.entities.JuicioResponses.GetJuicioResponse
import design_principles.actor_model.Query

sealed trait JuicioQueries extends Query with JuicioMessage

object JuicioQueries {
  case class GetStateJuicio(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      juicioId: String
  ) extends JuicioQueries {
    override type ReturnType = GetJuicioResponse
  }
}
