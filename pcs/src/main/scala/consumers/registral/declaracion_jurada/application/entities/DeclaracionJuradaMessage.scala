package consumers.registral.declaracion_jurada.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaMessage.DeclaracionJuradaMessageRoots

trait DeclaracionJuradaMessage
    extends design_principles.actor_model.ShardedMessage
    with SujetoMessage
    with ObjetoMessage {
  def declaracionJuradaId: String

  override def aggregateRoot: String =
    DeclaracionJuradaMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      declaracionJuradaId
    ).toString
}

object DeclaracionJuradaMessage {

  case class DeclaracionJuradaMessageRoots(sujetoId: String,
                                           objetoId: String,
                                           tipoObjeto: String,
                                           declaracionJuradaId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-DeclaracionJurada-$declaracionJuradaId"
  }
  object DeclaracionJuradaMessageRoots {

    def extractor(persistenceId: String): DeclaracionJuradaMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-DeclaracionJurada-$declaracionJuradaId" =>
          DeclaracionJuradaMessageRoots(sujetoId, objetoId, tipoObjeto, declaracionJuradaId)
      }
  }
}
