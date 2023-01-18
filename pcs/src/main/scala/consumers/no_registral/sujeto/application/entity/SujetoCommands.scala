package consumers.no_registral.sujeto.application.entity

import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.{SujetoAnt, SujetoTri}
import ddd.Deliverable
import design_principles.actor_model.Command

sealed trait SujetoCommands extends Command with SujetoMessage with Deliverable

object SujetoCommands {

  case class SujetoUpdateFromTri(
      deliveryId: BigInt,
      sujetoId: String,
      registro: SujetoTri
  ) extends SujetoCommands

  case class SujetoUpdateFromAnt(
      deliveryId: BigInt,
      sujetoId: String,
      registro: SujetoAnt
  ) extends SujetoCommands

  case class SujetoUpdateFromObjeto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      saldoObjeto: BigDecimal,
      saldoObligaciones: BigDecimal
  ) extends SujetoCommands

  case class SujetoSetBajaFromObjeto(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String
  ) extends SujetoCommands

}
