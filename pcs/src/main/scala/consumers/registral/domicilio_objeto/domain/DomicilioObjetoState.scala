package consumers.registral.domicilio_objeto.domain

import java.time.LocalDateTime

import consumers.registral.domicilio_objeto.application.entities.{DomicilioObjetoExternalDto, DomicilioObjetoMessage}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class DomicilioObjetoState(
    registro: Option[DomicilioObjetoExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[DomicilioObjetoMessage, DomicilioObjetoEvents, DomicilioObjetoState]
