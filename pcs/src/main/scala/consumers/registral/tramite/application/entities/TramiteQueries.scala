package consumers.registral.tramite.application.entities

import consumers.registral.tramite.application.entities.TramiteResponses.GetTramiteResponse
import design_principles.actor_model.Query

sealed trait TramiteQueries extends Query with TramiteMessage

object TramiteQueries {
  case class GetStateTramite(
      sujetoId: String,
      tramiteId: String
  ) extends TramiteQueries {
    override type ReturnType = GetTramiteResponse
  }
}
