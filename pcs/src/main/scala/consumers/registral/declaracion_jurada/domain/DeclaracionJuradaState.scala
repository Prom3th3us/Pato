package consumers.registral.declaracion_jurada.domain

import java.time.LocalDateTime

import consumers.registral.declaracion_jurada.application.entities.{
  DeclaracionJuradaExternalDto,
  DeclaracionJuradaMessage
}
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class DeclaracionJuradaState(
    registro: Option[DeclaracionJuradaExternalDto] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[DeclaracionJuradaMessage, DeclaracionJuradaEvents, DeclaracionJuradaState]
