package consumers.no_registral.objeto.infrastructure

import ai.x.play.json.Jsonx
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.application.entities.ObjetoCommands._
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{
  Exencion,
  ObjetosAnt,
  ObjetosTri,
  ObjetosTriOtrosAtributos
}
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetExencionResponse
import consumers.no_registral.objeto.domain.ObjetoEvents._
import io.leonard.TraitFormat
import io.leonard.TraitFormat.traitFormat
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val localdatetimeF = serialization.advanced.LocalDateTimeSerializer.dateFormat

  implicit val ExencionF = Json.format[Exencion]
  implicit val ObjetoAddExencionF = Json.format[ObjetoAddExencion]
  implicit val ObjetoAddedExencionF = Json.format[ObjetoAddedExencion]
  class ObjetoAddedExencionFS extends EventSerializer[ObjetoAddedExencion]

  implicit val ObjetosTriF = Jsonx.formatCaseClass[ObjetosTri]
  implicit val ObjetosAntF = Jsonx.formatCaseClass[ObjetosAnt]
  implicit val ObjetosDto: TraitFormat[ObjetoExternalDto] =
    (traitFormat[ObjetoExternalDto]
    << ObjetosTriF
    << ObjetosAntF)

  implicit val GetObjetoResponseF =
    Json.format[consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse]

  implicit val ObjetoSnapshotF = Json.format[ObjetoSnapshot]
  implicit val ObjetoUpdateCotitularesF = Json.format[ObjetoUpdateCotitulares]
  implicit val ObjetoUpdatedCotitularesF = Json.format[ObjetoUpdatedCotitulares]
  class ObjetoUpdatedCotitularesFS extends EventSerializer[ObjetoUpdatedCotitulares]
  implicit val ObjetoUpdateFromTriF = Json.format[ObjetoUpdateFromTri]
  implicit val ObjetoUpdateFromAntF = Json.format[ObjetoUpdateFromAnt]
  implicit val ObjetoUpdateFromObligacionF = Json.format[ObjetoUpdateFromObligacion]
  implicit val ObjetoTagAddF = Json.format[ObjetoTagAdd]
  implicit val SetBajaObjetoF = Json.format[SetBajaObjeto]
  implicit val ObjetoRemoveObligacionF = Json.format[ObjetoRemoveObligacion]
  implicit val ObjetoTagRemoveF = Json.format[ObjetoTagRemove]
  implicit val SelfUpdateCotitularesF = Json.format[SelfUpdateCotitulares]

  implicit val ObjetoSnapshotPersistedF = Json.format[ObjetoSnapshotPersisted]
  class ObjetoSnapshotPersistedFS extends EventSerializer[ObjetoSnapshotPersisted]

  implicit val ObjetoUpdatedFromTriF = Json.format[ObjetoUpdatedFromTri]
  class ObjetoUpdatedFromTriFS extends EventSerializer[ObjetoUpdatedFromTri]

  implicit val ObjetoUpdatedFromAntF = Json.format[ObjetoUpdatedFromAnt]
  class ObjetoUpdatedFromAntFS extends EventSerializer[ObjetoUpdatedFromAnt]

  implicit val ObjetoTagAddedF = Json.format[ObjetoTagAdded]
  class ObjetoTagAddedFS extends EventSerializer[ObjetoTagAdded]

  implicit val ObjetoTagRemovedF = Json.format[ObjetoTagRemoved]
  class ObjetoTagRemovedFS extends EventSerializer[ObjetoTagRemoved]

  implicit val ObjetoUpdatedFromObligacionF = Json.format[ObjetoUpdatedFromObligacion]
  class ObjetoUpdatedFromObligacionFS extends EventSerializer[ObjetoUpdatedFromObligacion]

  implicit val ObjetoUpdatedFromObligacionBajaSetF = Json.format[ObjetoUpdatedFromObligacionBajaSet]
  class ObjetoUpdatedFromObligacionBajaSetFS extends EventSerializer[ObjetoUpdatedFromObligacionBajaSet]

  implicit val ObjetosTriOtrosAtributosF = Json.format[ObjetosTriOtrosAtributos]

  implicit val GetExencionResponseF = Json.format[GetExencionResponse]

  implicit val ObjetoBajaSetF = Json.format[ObjetoBajaSet]
  class ObjetoBajaSetFS extends EventSerializer[ObjetoBajaSet]

  implicit val ObjetoRemovedObligacionF = Json.format[ObjetoRemovedObligacion]
  class ObjetoRemovedObligacionFS extends EventSerializer[ObjetoRemovedObligacion]

}
