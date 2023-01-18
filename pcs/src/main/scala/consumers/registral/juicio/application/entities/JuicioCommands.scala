package consumers.registral.juicio.application.entities

import akka.Done
import akka.actor.typed.ActorRef
import consumers.registral.juicio.application.entities.JuicioExternalDto.DetallesJuicio

sealed trait JuicioCommands extends design_principles.actor_model.Command with JuicioMessage
object JuicioCommands {
  case class JuicioUpdateFromDto(sujetoId: String,
                                 objetoId: String,
                                 tipoObjeto: String,
                                 juicioId: String,
                                 deliveryId: BigInt,
                                 registro: JuicioExternalDto,
                                 detallesJuicio: Seq[DetallesJuicio])
      extends JuicioCommands

}
