package consumers.registral.etapas_procesales.infrastructure

import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesCommands.EtapasProcesalesUpdateFromDto
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto.{
  EtapasProcesalesAnt,
  EtapasProcesalesTri
}
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesResponses.GetEtapasProcesalesResponse
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val EtapasProcesalesTriF = Json.format[EtapasProcesalesTri]
  implicit val EtapasProcesalesAntF = Json.format[EtapasProcesalesAnt]

  import io.leonard.TraitFormat
  import io.leonard.TraitFormat.traitFormat
  implicit val EtapasProcesalesExternalDtoF: TraitFormat[EtapasProcesalesExternalDto] =
    (traitFormat[EtapasProcesalesExternalDto]
    << EtapasProcesalesAntF
    << EtapasProcesalesTriF)

  implicit val EtapasProcesalesUpdateFromDtoF = Json.format[EtapasProcesalesUpdateFromDto]
  implicit val EtapasProcesalesUpdatedFromDtoF = Json.format[EtapasProcesalesUpdatedFromDto]
  class EtapasProcesalesUpdatedFromDtoFS extends EventSerializer[EtapasProcesalesUpdatedFromDto]

  implicit val GetEtapasProcesalesResponseF = Json.format[GetEtapasProcesalesResponse]
}
