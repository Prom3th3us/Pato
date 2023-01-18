package consumers.registral.parametrica_recargo.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.parametrica_recargo.application.cqrs.commands.ParametricaRecargoUpdateFromDtoHandler
import consumers.registral.parametrica_recargo.application.cqrs.queries.GetStateParametricaRecargoHandler
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoCommands.ParametricaRecargoUpdateFromDto
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoMessage
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoQueries.GetStateParametricaRecargo
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto
import consumers.registral.parametrica_recargo.domain.events.ParametricaRecargoUpdatedFromDtoHandler
import consumers.registral.parametrica_recargo.domain.{ParametricaRecargoEvents, ParametricaRecargoState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class ParametricaRecargoActor(state: ParametricaRecargoState = ParametricaRecargoState())(
    implicit

    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      ParametricaRecargoMessage,
      ParametricaRecargoEvents,
      ParametricaRecargoState
    ](state) {
  commandBus.subscribe[ParametricaRecargoUpdateFromDto](new ParametricaRecargoUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateParametricaRecargo](new GetStateParametricaRecargoHandler().handle)
  eventBus.subscribe[ParametricaRecargoUpdatedFromDto](new ParametricaRecargoUpdatedFromDtoHandler().handle)
}
