package consumers.no_registral.sujeto.infrastructure

import ai.x.play.json.Jsonx
import consumers.no_registral.sujeto.application.entity.SujetoCommands.{
  SujetoSetBajaFromObjeto,
  SujetoUpdateFromAnt,
  SujetoUpdateFromObjeto,
  SujetoUpdateFromTri
}
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.{SujetoAnt, SujetoTri}
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import consumers.no_registral.sujeto.domain.SujetoEvents.{
  SujetoBajaFromObjetoSet,
  SujetoSnapshotPersisted,
  SujetoUpdatedFromAnt,
  SujetoUpdatedFromObjeto,
  SujetoUpdatedFromTri
}
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val SujetosTriF = Jsonx.formatCaseClass[SujetoTri]
  implicit val SujetosAntF = Jsonx.formatCaseClass[SujetoAnt]
  implicit val SujetoUpdateFromObjetoF = Jsonx.formatCaseClass[SujetoUpdateFromObjeto]

  implicit val SujetosDto: TraitFormat[SujetoExternalDto] =
    (traitFormat[SujetoExternalDto]
    << SujetosTriF
    << SujetosAntF)

  implicit val sujetoUpdateFromTriF = Json.format[SujetoUpdateFromTri]
  implicit val sujetoUpdateFromAntF = Json.format[SujetoUpdateFromAnt]
  implicit val sujetoUpdateFromObjetoF = Json.format[SujetoUpdateFromObjeto]
  implicit val sujetoSetBajaFromObjetoF = Json.format[SujetoSetBajaFromObjeto]

  implicit val sujetoSnapshotPersistedF = Json.format[SujetoSnapshotPersisted]
  class SujetoSnapshotPersistedFS extends EventSerializer[SujetoSnapshotPersisted]
  implicit val sujetoUpdatedFromTriF = Json.format[SujetoUpdatedFromTri]
  class SujetoUpdatedFromTriFS extends EventSerializer[SujetoUpdatedFromTri]
  implicit val sujetoUpdatedFromAntF = Json.format[SujetoUpdatedFromAnt]
  class SujetoUpdatedFromAntFS extends EventSerializer[SujetoUpdatedFromAnt]
  implicit val sujetoUpdatedFromObjetoF = Json.format[SujetoUpdatedFromObjeto]
  class SujetoUpdatedFromObjetoFS extends EventSerializer[SujetoUpdatedFromObjeto]
  implicit val sujetoBajaFromObjetoSetF = Json.format[SujetoBajaFromObjetoSet]
  class SujetoBajaFromObjetoSetFS extends EventSerializer[SujetoBajaFromObjetoSet]

  implicit val GetSujetoResponseF = Json.format[GetSujetoResponse]

}
