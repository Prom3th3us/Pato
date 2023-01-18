package consumers.registral.actividad_sujeto.application.entities

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoResponses.GetActividadSujetoResponse
import design_principles.actor_model.Query

trait ActividadSujetoQueries extends Query with ActividadSujetoMessage

object ActividadSujetoQueries {
  case class GetStateActividadSujeto(
      sujetoId: String,
      actividadSujetoId: String
  ) extends ActividadSujetoQueries {
    override type ReturnType = GetActividadSujetoResponse
  }
}
