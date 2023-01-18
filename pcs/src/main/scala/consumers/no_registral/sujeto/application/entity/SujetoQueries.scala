package consumers.no_registral.sujeto.application.entity

import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import design_principles.actor_model.Query

sealed trait SujetoQueries extends Query with SujetoMessage

object SujetoQueries {
  case class GetStateSujeto(sujetoId: String) extends SujetoQueries {
    override type ReturnType = GetSujetoResponse
  }
  case class GetSnapshotSujeto(sujetoId: String) extends SujetoQueries {
    override type ReturnType = GetSujetoResponse
  }
}
