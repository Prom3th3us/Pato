package consumers.registral.parametrica_recargo.domain

import java.time.LocalDateTime

import consumers.registral.parametrica_recargo.application.entities.{
  ParametricaRecargoExternalDto,
  ParametricaRecargoMessage
}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class ParametricaRecargoState(
    registro: Option[ParametricaRecargoExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[ParametricaRecargoMessage, ParametricaRecargoEvents, ParametricaRecargoState]
