package consumers.registral.juicio.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import consumers.registral.juicio.application.cqrs.commands.JuicioUpdateFromDtoHandler
import consumers.registral.juicio.application.cqrs.queries.GetStateJuicioHandler
import consumers.registral.juicio.application.entities.JuicioCommands.JuicioUpdateFromDto
import consumers.registral.juicio.application.entities.JuicioMessage
import consumers.registral.juicio.application.entities.JuicioQueries.GetStateJuicio
import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import consumers.registral.juicio.domain.events.JuicioUpdatedFromDtoHandler
import consumers.registral.juicio.domain.{JuicioEvents, JuicioState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class JuicioActor(state: JuicioState = JuicioState())(
    implicit
    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      JuicioMessage,
      JuicioEvents,
      JuicioState
    ](state) {

  commandBus.subscribe[JuicioUpdateFromDto](new JuicioUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateJuicio](new GetStateJuicioHandler().handle)
  eventBus.subscribe[JuicioUpdatedFromDto](new JuicioUpdatedFromDtoHandler().handle)
}
