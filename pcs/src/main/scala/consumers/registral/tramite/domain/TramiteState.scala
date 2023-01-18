package consumers.registral.tramite.domain

import java.time.LocalDateTime

import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import consumers.registral.tramite.application.entities.TramiteMessage
import cqrs.base_actor.typed.AbstractStateWithCQRS

case class TramiteState(
    registro: Option[Tramite] = None,
    fechaUltMod: LocalDateTime = LocalDateTime.MIN
) extends AbstractStateWithCQRS[TramiteMessage, TramiteEvents, TramiteState]
