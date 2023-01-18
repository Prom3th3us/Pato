package consumers.registral.subasta.application.entities

import java.time.LocalDateTime

sealed trait SubastaResponses
object SubastaResponses {

  case class GetSubastaResponse(registro: Option[SubastaExternalDto] = None, fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
