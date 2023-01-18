package consumers.registral.etapas_procesales.application.entities

import design_principles.actor_model.Command

sealed trait EtapasProcesalesCommands extends Command with EtapasProcesalesMessage

object EtapasProcesalesCommands {
  case class EtapasProcesalesUpdateFromDto(
      juicioId: String,
      etapaId: String,
      deliveryId: BigInt,
      registro: EtapasProcesalesExternalDto
  ) extends EtapasProcesalesCommands
}
