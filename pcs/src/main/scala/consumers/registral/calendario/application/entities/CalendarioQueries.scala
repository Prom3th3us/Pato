package consumers.registral.calendario.application.entities

import consumers.registral.calendario.application.entities.CalendarioResponses.GetCalendarioResponse
import design_principles.actor_model.Query

sealed trait CalendarioQueries extends CalendarioMessage with Query
object CalendarioQueries {
  case class GetStateCalendario(calendarioId: String) extends CalendarioQueries {
    override type ReturnType = GetCalendarioResponse
  }
}
