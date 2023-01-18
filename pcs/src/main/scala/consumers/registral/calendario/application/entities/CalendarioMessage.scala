package consumers.registral.calendario.application.entities

import consumers.registral.calendario.application.entities.CalendarioMessage.CalendarioMessageRoots

trait CalendarioMessage extends design_principles.actor_model.ShardedMessage {

  val calendarioId: String

  override def aggregateRoot: String =
    CalendarioMessageRoots(
      calendarioId
    ).toString
}

object CalendarioMessage {

  case class CalendarioMessageRoots(calendarioId: String) {
    override def toString = s"Calendario-$calendarioId"
  }
  object CalendarioMessageRoots {

    def extractor(persistenceId: String): CalendarioMessageRoots =
      persistenceId match {
        case s"Calendario-$calendarioId" =>
          CalendarioMessageRoots(calendarioId)
      }
  }
}
