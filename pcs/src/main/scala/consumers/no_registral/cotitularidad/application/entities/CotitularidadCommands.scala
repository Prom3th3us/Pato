package consumers.no_registral.cotitularidad.application.entities

import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted

sealed trait CotitularidadCommands extends design_principles.actor_model.Command {
  def objetoId: String
  def tipoObjeto: String
  def aggregateRoot = s"Objeto-$objetoId-$tipoObjeto"
}

object CotitularidadCommands {

  case class ObjetoSnapshotPersistedReaction(
      deliveryId: BigInt,
      objetoId: String,
      tipoObjeto: String,
      event: ObjetoSnapshotPersisted
  ) extends CotitularidadCommands

  case class CotitularidadPublishSnapshot(
      deliveryId: BigInt,
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      saldo: BigDecimal,
      tags: Set[String],
      obligacionesSaldo: Map[String, BigDecimal] = Map.empty
  ) extends CotitularidadCommands
}
