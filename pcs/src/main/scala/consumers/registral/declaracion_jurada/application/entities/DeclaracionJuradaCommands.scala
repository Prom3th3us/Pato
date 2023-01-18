package consumers.registral.declaracion_jurada.application.entities

import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaExternalDto.DeclaracionJurada

sealed trait DeclaracionJuradaCommands extends design_principles.actor_model.Command with DeclaracionJuradaMessage
object DeclaracionJuradaCommands {
  case class DeclaracionJuradaUpdateFromDto(sujetoId: String,
                                            objetoId: String,
                                            tipoObjeto: String,
                                            declaracionJuradaId: String,
                                            deliveryId: BigInt,
                                            registro: DeclaracionJurada)
      extends DeclaracionJuradaCommands

}
