package consumers.registral.declaracion_jurada.application.entities

import java.time.LocalDateTime

sealed trait DeclaracionJuradaResponses
object DeclaracionJuradaResponses {

  case class GetDeclaracionJuradaResponse(registro: Option[DeclaracionJuradaExternalDto] = None,
                                          fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
