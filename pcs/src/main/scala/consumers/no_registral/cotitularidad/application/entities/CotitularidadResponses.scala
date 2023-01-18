package consumers.no_registral.cotitularidad.application.entities

import java.time.LocalDateTime

sealed trait CotitularidadResponses extends CotitularidadMessage with design_principles.actor_model.Response

object CotitularidadResponses {

  case class GetCotitularesResponse(
      objetoId: String,
      tipoObjeto: String,
      sujetosCotitulares: Set[String],
      sujetoResponsable: String,
      fechaUltMod: LocalDateTime
  ) extends CotitularidadResponses

}
