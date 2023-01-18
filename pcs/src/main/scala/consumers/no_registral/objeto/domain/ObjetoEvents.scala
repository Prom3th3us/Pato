package consumers.no_registral.objeto.domain

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{Exencion, ObjetosAnt, ObjetosTri}
import design_principles.actor_model.Event

sealed trait ObjetoEvents extends Event {
  def sujetoId: String
  def objetoId: String
  def tipoObjeto: String
  def deliveryId: BigInt
  def aggregateRoot: String = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto"
}

object ObjetoEvents {
  val operaciones: Map[String, String] = Map(("Upsert" -> "U"), ("Delete" -> "D"))

  case class ObjetoUpdatedCotitulares(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      cotitulares: Set[String]
  ) extends ObjetoEvents

  case class ObjetoSnapshotPersisted(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      objetoId2: Option[String],
      tipoObjeto: String,
      saldo: BigDecimal,
      cotitulares: Set[String],
      tags: Set[String],
      sujetoResponsable: Option[String],
      porcentajeResponsabilidad: BigDecimal,
      registro: Option[ObjetoExternalDto],
      obligacionesSaldo: Map[String, BigDecimal] = Map.empty,
      cuotas:List[Boolean],
      operacion: String,
      idExterno: Option[String]
  ) extends ObjetoEvents

  case class ObjetoUpdatedFromTri(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetosTri,
      isResponsable: Option[Boolean],
      sujetoResponsable: Option[String],
      isAdheridoDebito: Option[Boolean],
  ) extends ObjetoEvents

  case class ObjetoUpdatedFromAnt(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetosAnt
  ) extends ObjetoEvents

  case class ObjetoTagAdded(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      tagAdded: String
  ) extends ObjetoEvents

  case class ObjetoTagRemoved(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      tagRemoved: String
  ) extends ObjetoEvents

  case class ObjetoUpdatedFromObligacion(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      objetoId2: Option[String],
      tipoObjeto: String,
      obligacionId: String,
      saldoObligacion: BigDecimal,
      obligacionExenta: Boolean,
      porcentajeExencion: Option[BigDecimal],
      idExterno: Option[String]
  ) extends ObjetoEvents

  case class ObjetoUpdatedFromObligacionBajaSet(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      obligacionId: String
  ) extends ObjetoEvents

  case class ObjetoAddedExencion(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      exencion: Exencion
  ) extends ObjetoEvents

  case class ObjetoBajaSet(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetoExternalDto,
      isResponsable: Option[Boolean],
      sujetoResponsable: Option[String]
  ) extends ObjetoEvents

  case class ObjetoRemovedObligacion(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      obligacionId: String,
      cuota:Option[String]
  ) extends ObjetoEvents

}
