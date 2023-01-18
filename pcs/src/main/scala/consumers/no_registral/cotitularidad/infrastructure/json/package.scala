package consumers.no_registral.cotitularidad.infrastructure

import consumers.no_registral.cotitularidad.application.entities.CotitularidadCommands.{CotitularidadPublishSnapshot}
import consumers.no_registral.cotitularidad.application.entities.CotitularidadQueries.GetCotitulares
import consumers.no_registral.cotitularidad.application.entities.CotitularidadResponses.GetCotitularesResponse
import consumers.no_registral.cotitularidad.domain.CotitularidadEvents.CotitularidadAddedSujetoCotitular
import play.api.libs.json.Json
import serialization.EventSerializer

package object json {
  implicit val CotitularPublishSnapshotF = Json.format[CotitularidadPublishSnapshot]

  implicit val CotitularidadAddedSujetoCotitularF = Json.format[CotitularidadAddedSujetoCotitular]
  class CotitularidadAddedSujetoCotitularFS extends EventSerializer[CotitularidadAddedSujetoCotitular]
  implicit val GetCotitularesF = Json.format[GetCotitulares]
  implicit val GetCotitularesResponseF = Json.format[GetCotitularesResponse]
}
