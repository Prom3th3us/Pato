package consumers.registral.etapas_procesales.application.entities

import java.time.LocalDateTime

sealed trait EtapasProcesalesResponses
object EtapasProcesalesResponses {

  case class GetEtapasProcesalesResponse(registro: Option[EtapasProcesalesExternalDto] = None,
                                         fechaUltMod: LocalDateTime)
      extends design_principles.actor_model.Response
}
