package consumers.registral.calendario.domain

import java.time.LocalDateTime

import consumers.registral.calendario.application.entities.{CalendarioExternalDto, CalendarioMessage}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class CalendarioState(
    registro: Option[CalendarioExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.now
) extends AbstractStateWithCQRS[CalendarioMessage, CalendarioEvents, CalendarioState]
