package consumers.registral.etapas_procesales.infrastructure.dependency_injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config
import consumers.registral.etapas_procesales.application.cqrs.commands.EtapasProcesalesUpdateFromDtoHandler
import consumers.registral.etapas_procesales.application.cqrs.queries.GetStateEtapasProcesalesHandler
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesCommands.EtapasProcesalesUpdateFromDto
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesMessage
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesQueries.GetStateEtapasProcesales
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import consumers.registral.etapas_procesales.domain.events.EtapasProcesalesUpdatedFromDtoHandler
import consumers.registral.etapas_procesales.domain.{EtapasProcesalesEvents, EtapasProcesalesState}
import cqrs.base_actor.typed.BasePersistentShardedTypedActorWithCQRS
import kafka.MessageProducer

case class EtapasProcesalesActor(state: EtapasProcesalesState = EtapasProcesalesState())(
    implicit

    messageProducer: MessageProducer,
    system: ActorSystem[Nothing]
) extends BasePersistentShardedTypedActorWithCQRS[
      EtapasProcesalesMessage,
      EtapasProcesalesEvents,
      EtapasProcesalesState
    ](state) {

  commandBus.subscribe[EtapasProcesalesUpdateFromDto](new EtapasProcesalesUpdateFromDtoHandler().handle)
  queryBus.subscribe[GetStateEtapasProcesales](new GetStateEtapasProcesalesHandler().handle)
  eventBus.subscribe[EtapasProcesalesUpdatedFromDto](new EtapasProcesalesUpdatedFromDtoHandler().handle)
}
