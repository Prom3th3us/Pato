package consumers.no_registral.cotitularidad.application.entities

sealed trait CotitularidadQueries extends CotitularidadMessage with design_principles.actor_model.Query

object CotitularidadQueries {
  case class GetCotitulares(
      objetoId: String,
      tipoObjeto: String
  ) extends CotitularidadQueries
}
