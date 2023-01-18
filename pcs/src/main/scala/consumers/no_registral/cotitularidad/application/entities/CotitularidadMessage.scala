package consumers.no_registral.cotitularidad.application.entities

import consumers.no_registral.cotitularidad.application.entities.CotitularidadMessage.CotitularidadMessageRoots

trait CotitularidadMessage extends design_principles.actor_model.ShardedMessage {
  def objetoId: String
  def tipoObjeto: String
  override def aggregateRoot: String =
    CotitularidadMessageRoots(
      objetoId,
      tipoObjeto
    ).toString
}

object CotitularidadMessage {
  case class CotitularidadMessageRoots(objetoId: String, tipoObjeto: String) {
    override def toString: String = s"Objeto-$objetoId-$tipoObjeto"
  }
  object CotitularidadMessageRoots {
    def extractor(persistenceId: String): CotitularidadMessageRoots =
      persistenceId match {
        case s"Objeto-$objetoId-$tipoObjeto" =>
          CotitularidadMessageRoots(objetoId, tipoObjeto)
      }
  }
}
