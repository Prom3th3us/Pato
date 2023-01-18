package consumers.registral.juicio.domain

import consumers.registral.juicio.application.entities.{JuicioExternalDto, JuicioMessage}
import consumers.registral.juicio.application.entities.JuicioExternalDto.DetallesJuicio
import design_principles.actor_model.Event

sealed trait JuicioEvents extends Event with JuicioMessage

object JuicioEvents {
  case class JuicioUpdatedFromDto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      juicioId: String,
      registro: JuicioExternalDto,
      detallesJuicio: Seq[DetallesJuicio]
  ) extends JuicioEvents
}
