package consumers.registral.juicio.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.juicio.application.entities.JuicioMessage.JuicioMessageRoots

trait JuicioMessage extends SujetoMessage with ObjetoMessage {

  val juicioId: String

  override def aggregateRoot: String =
    JuicioMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      juicioId
    ).toString
}

object JuicioMessage {

  case class JuicioMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String, juicioId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Juicio-$juicioId"
  }
  object JuicioMessageRoots {

    def extractor(persistenceId: String): JuicioMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-Juicio-$juicioId" =>
          JuicioMessageRoots(sujetoId, objetoId, tipoObjeto, juicioId)
      }
  }
}
