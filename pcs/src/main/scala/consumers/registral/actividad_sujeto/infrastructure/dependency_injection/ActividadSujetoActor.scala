package consumers.registral.actividad_sujeto.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.actividad_sujeto.application.cqrs.commands.ActividadSujetoUpdateFromDtoHandler
import consumers.registral.actividad_sujeto.application.cqrs.queries.GetStateActividadSujetoHandler
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoCommands.ActividadSujetoUpdateFromDto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoMessage
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoQueries.GetStateActividadSujeto
import consumers.registral.actividad_sujeto.domain.ActividadSujetoEvents.ActividadSujetoUpdatedFromDto
import consumers.registral.actividad_sujeto.domain.events.ActividadSujetoUpdatedFromDtoHandler
import consumers.registral.actividad_sujeto.domain.{ActividadSujetoEvents, ActividadSujetoState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class ActividadSujetoActor(state: ActividadSujetoState = ActividadSujetoState())(
    implicit
    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      ActividadSujetoMessage,
      ActividadSujetoEvents,
      ActividadSujetoState
    ](state) {
  commandBus.subscribe[ActividadSujetoUpdateFromDto](new ActividadSujetoUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateActividadSujeto](new GetStateActividadSujetoHandler().handle)
  eventBus.subscribe[ActividadSujetoUpdatedFromDto](new ActividadSujetoUpdatedFromDtoHandler().handle)
}
