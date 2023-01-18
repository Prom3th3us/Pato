package consumers.registral.juicio.application.entities

import java.time.LocalDateTime

import consumers.registral.juicio.application.entities.JuicioExternalDto.DetallesJuicio

sealed trait JuicioResponses
object JuicioResponses {

  case class GetJuicioResponse(registro: Option[JuicioExternalDto] = None,
                               detallesJuicio: Seq[DetallesJuicio],
                               fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response

}
