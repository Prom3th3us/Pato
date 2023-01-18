package consumers.registral.domicilio_sujeto.domain

import java.time.LocalDateTime

import consumers.registral.domicilio_sujeto.application.entities.{DomicilioSujetoExternalDto, DomicilioSujetoMessage}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class DomicilioSujetoState(
    registro: Option[DomicilioSujetoExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[DomicilioSujetoMessage, DomicilioSujetoEvents, DomicilioSujetoState]
