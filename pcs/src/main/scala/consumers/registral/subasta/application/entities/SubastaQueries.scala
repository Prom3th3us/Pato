package consumers.registral.subasta.application.entities

import consumers.registral.subasta.application.entities.SubastaResponses.GetSubastaResponse
import design_principles.actor_model.Query

sealed trait SubastaQueries extends Query with SubastaMessage {

  val sujetoId: String
  val tipoObjeto: String
  val objetoId: String
  val subastaId: String

  override def aggregateRoot: String = s"$sujetoId-$objetoId-$tipoObjeto-$subastaId"
}

object SubastaQueries {

  case class GetStateSubasta(
      sujetoId: String,
      objetoId: String,
      tipoObjeto: String,
      subastaId: String
  ) extends SubastaQueries {
    override type ReturnType = GetSubastaResponse
  }
}
