package consumers.registral.etapas_procesales.application.entities

import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesResponses.GetEtapasProcesalesResponse
import design_principles.actor_model.Query

sealed trait EtapasProcesalesQueries extends Query with EtapasProcesalesMessage

object EtapasProcesalesQueries {
  case class GetStateEtapasProcesales(
      juicioId: String,
      etapaId: String
  ) extends EtapasProcesalesQueries {
    override type ReturnType = GetEtapasProcesalesResponse
  }
}
