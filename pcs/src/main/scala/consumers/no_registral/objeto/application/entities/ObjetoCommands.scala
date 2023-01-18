package consumers.no_registral.objeto.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{Exencion, ObjetosAnt, ObjetosTri}
import design_principles.actor_model.Command

sealed trait ObjetoCommands extends Command with ObjetoMessage

object ObjetoCommands {

  case class ObjetoUpdateCotitulares(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      cotitulares: Set[String]
  ) extends ObjetoCommands

  case class ObjetoSnapshot(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      objetoId2: Option[String],
      tipoObjeto: String,
      saldo: BigDecimal,
      cotitulares: Set[String],
      tags: Set[String],
      sujetoResponsable: Option[String],
      obligacionesSaldo: Map[String, BigDecimal] = Map.empty,
      idExterno: Option[String]
  ) extends ObjetoCommands

  case class ObjetoUpdateFromTri(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetosTri,
      isResponsable: Option[Boolean],
      sujetoResponsable: Option[String],
      isAdheridoDebito: Option[Boolean],
  ) extends ObjetoCommands

  case class ObjetoUpdateFromAnt(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetosAnt
  ) extends ObjetoCommands

  case class ObjetoUpdateFromObligacion(
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
  ) extends ObjetoCommands

  case class ObjetoRemoveObligacion(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      obligacionId: String,
      cuota: Option[String]
  ) extends ObjetoCommands

  case class ObjetoTagAdd(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      tag: String
  ) extends ObjetoCommands

  case class ObjetoTagRemove(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      tag: String
  ) extends ObjetoCommands

  case class SelfUpdateCotitulares(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String
  ) extends ObjetoCommands

  case class ObjetoAddExencion(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      exencion: Exencion
  ) extends ObjetoCommands

  case class SetBajaObjeto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      registro: ObjetoExternalDto,
      isResponsable: Option[Boolean],
      sujetoResponsable: Option[String]
  ) extends ObjetoCommands

}
