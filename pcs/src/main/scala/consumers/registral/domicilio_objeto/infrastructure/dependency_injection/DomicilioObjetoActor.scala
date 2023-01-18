package consumers.registral.domicilio_objeto.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.domicilio_objeto.application.cqrs.commands.DomicilioObjetoUpdateFromDtoHandler
import consumers.registral.domicilio_objeto.application.cqrs.queries.GetStateDomicilioObjetoHandler
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoCommands.DomicilioObjetoUpdateFromDto
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoMessage
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoQueries.GetStateDomicilioObjeto
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto
import consumers.registral.domicilio_objeto.domain.events.DomicilioObjetoUpdatedFromDtoHandler
import consumers.registral.domicilio_objeto.domain.{DomicilioObjetoEvents, DomicilioObjetoState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class DomicilioObjetoActor(state: DomicilioObjetoState = DomicilioObjetoState())(
    implicit

    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      DomicilioObjetoMessage,
      DomicilioObjetoEvents,
      DomicilioObjetoState
    ](state) {

  commandBus.subscribe[DomicilioObjetoUpdateFromDto](new DomicilioObjetoUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateDomicilioObjeto](new GetStateDomicilioObjetoHandler().handle)
  eventBus.subscribe[DomicilioObjetoUpdatedFromDto](new DomicilioObjetoUpdatedFromDtoHandler().handle)
}
