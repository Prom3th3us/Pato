package consumers.no_registral.cotitularidad.domain

import design_principles.actor_model.Event

sealed trait CotitularidadEvents extends Event {
  def objetoId: String
  def tipoObjeto: String
  def aggregateRoot: String = s"Objeto-$objetoId-$tipoObjeto"
}

object CotitularidadEvents {
  case class CotitularidadAddedSujetoCotitular(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      isResponsable: Option[Boolean],
      sujetoResponsable: Option[String]
  ) extends CotitularidadEvents

}
