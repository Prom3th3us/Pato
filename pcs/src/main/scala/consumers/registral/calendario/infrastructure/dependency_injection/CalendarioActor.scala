package consumers.registral.calendario.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.calendario.application.cqrs.commands.CalendarioUpdateFromDtoHandler
import consumers.registral.calendario.application.cqrs.queries.GetStateCalendarioHandler
import consumers.registral.calendario.application.entities.CalendarioCommands.CalendarioUpdateFromDto
import consumers.registral.calendario.application.entities.CalendarioMessage
import consumers.registral.calendario.application.entities.CalendarioQueries.GetStateCalendario
import consumers.registral.calendario.domain.CalendarioEvents.CalendarioUpdatedFromDto
import consumers.registral.calendario.domain.events.CalendarioUpdatedFromDtoHandler
import consumers.registral.calendario.domain.{CalendarioEvents, CalendarioState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class CalendarioActor(state: CalendarioState = CalendarioState())(
    implicit

    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      CalendarioMessage,
      CalendarioEvents,
      CalendarioState
    ](state) {
  commandBus.subscribe[CalendarioUpdateFromDto](new CalendarioUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateCalendario](new GetStateCalendarioHandler().handle)
  eventBus.subscribe[CalendarioUpdatedFromDto](new CalendarioUpdatedFromDtoHandler().handle)
}
