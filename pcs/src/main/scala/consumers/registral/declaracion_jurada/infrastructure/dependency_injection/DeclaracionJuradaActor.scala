package consumers.registral.declaracion_jurada.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.declaracion_jurada.application.cqrs.commands.DeclaracionJuradaUpdateFromDtoHandler
import consumers.registral.declaracion_jurada.application.cqrs.queries.GetStateDeclaracionJuradaHandler
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaCommands.DeclaracionJuradaUpdateFromDto
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaMessage
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaQueries.GetStateDeclaracionJurada
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto
import consumers.registral.declaracion_jurada.domain.events.DeclaracionJuradaUpdatedFromDtoHandler
import consumers.registral.declaracion_jurada.domain.{DeclaracionJuradaEvents, DeclaracionJuradaState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class DeclaracionJuradaActor(state: DeclaracionJuradaState = DeclaracionJuradaState())(
    implicit

    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      DeclaracionJuradaMessage,
      DeclaracionJuradaEvents,
      DeclaracionJuradaState
    ](state) {

  commandBus.subscribe[DeclaracionJuradaUpdateFromDto](new DeclaracionJuradaUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateDeclaracionJurada](new GetStateDeclaracionJuradaHandler().handle)
  eventBus.subscribe[DeclaracionJuradaUpdatedFromDto](new DeclaracionJuradaUpdatedFromDtoHandler().handle)
}
