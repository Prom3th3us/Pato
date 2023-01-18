package consumers.registral.actividad_sujeto.domain

import java.time.LocalDateTime

import consumers.registral.actividad_sujeto.application.entities.{ActividadSujetoExternalDto, ActividadSujetoMessage}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class ActividadSujetoState(
    registro: Option[ActividadSujetoExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[ActividadSujetoMessage, ActividadSujetoEvents, ActividadSujetoState]
