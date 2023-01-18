package consumers.registral.etapas_procesales.application.entities

import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesMessage.EtapasProcesalesMessageRoots
import design_principles.actor_model.ShardedMessage

trait EtapasProcesalesMessage extends ShardedMessage {
  def juicioId: String
  def etapaId: String

  override def aggregateRoot: String =
    EtapasProcesalesMessageRoots(
      juicioId,
      etapaId
    ).toString
}
object EtapasProcesalesMessage {

  case class EtapasProcesalesMessageRoots(juicioId: String, etapaId: String) {
    override def toString = s"Juicio-$juicioId-EtapaProcesal-$etapaId"
  }
  object EtapasProcesalesMessageRoots {

    def extractor(persistenceId: String): EtapasProcesalesMessageRoots =
      persistenceId match {
        case s"Juicio-$juicioId-EtapaProcesal-$etapaId" =>
          EtapasProcesalesMessageRoots(juicioId, etapaId)
      }
  }
}
