package consumers.registral.parametrica_plan.domain

import java.time.LocalDateTime

import consumers.registral.parametrica_plan.application.entities.{ParametricaPlanExternalDto, ParametricaPlanMessage}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class ParametricaPlanState(
    registro: Option[ParametricaPlanExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[ParametricaPlanMessage, ParametricaPlanEvents, ParametricaPlanState]
