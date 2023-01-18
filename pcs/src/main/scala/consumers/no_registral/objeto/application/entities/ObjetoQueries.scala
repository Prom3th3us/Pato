package consumers.no_registral.objeto.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoResponses.{GetExencionResponse, GetObjetoResponse}
import design_principles.actor_model.Query

sealed trait ObjetoQueries extends Query with ObjetoMessage

object ObjetoQueries {
  case class GetStateObjeto(sujetoId: String, objetoId: String, tipoObjeto: String) extends ObjetoQueries {
    override type ReturnType = GetObjetoResponse
  }
  case class GetStateExencion(sujetoId: String, objetoId: String, tipoObjeto: String, exencion: String)
      extends ObjetoQueries {
    override type ReturnType = GetExencionResponse
  }
  case class GetSnapshotObjeto(sujetoId: String, objetoId: String, tipoObjeto: String) extends ObjetoQueries {
    override type ReturnType = GetObjetoResponse
  }
}
