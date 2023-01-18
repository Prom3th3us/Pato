package consumers.no_registral.cotitularidad.infrastructure.dependency_injection

import akka.actor.Props
import akka.entity.ShardedEntity
import akka.entity.ShardedEntity.MonitoringAndMessageProducer
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import consumers.no_registral.cotitularidad.application.entities.CotitularidadCommands.ObjetoSnapshotPersistedReaction
import consumers.no_registral.cotitularidad.application.entities.{CotitularidadQueries, CotitularidadResponses}
import consumers.no_registral.cotitularidad.domain.CotitularidadEvents.CotitularidadAddedSujetoCotitular
import consumers.no_registral.cotitularidad.domain.{CotitularidadEvents, CotitularidadState}
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.Response
import kafka.KafkaMessageProducer.KafkaKeyValue

class CotitularidadActor(requirements: MonitoringAndMessageProducer)
    extends PersistentActor
    with akka.actor.ActorLogging {

  val monitoring = requirements.monitoring
  val messageProducer = requirements.messageProducer

  var state = CotitularidadState()

  def sujetosNoResponsables: Set[String] =
    state.sujetoResponsable match {
      case Some(s) => state.sujetosCotitulares.filter(_ != s)
      case None => Set.empty
    }

  override def receiveCommand: Receive = {

    case query: CotitularidadQueries.GetCotitulares =>
      sender() ! CotitularidadResponses.GetCotitularesResponse(
        query.objetoId,
        query.tipoObjeto,
        state.sujetosCotitulares,
        state.sujetoResponsable.getOrElse(""),
        state.fechaUltMod
      )
    case command: ObjetoSnapshotPersistedReaction
        if command.event.sujetoResponsable.isDefined
        && command.event.sujetoResponsable.get == command.event.sujetoId => {
      val replyTo = sender()

      val event = CotitularidadAddedSujetoCotitular(
        command.deliveryId,
        command.event.sujetoId,
        command.objetoId,
        command.tipoObjeto,
        isResponsable = command.event.sujetoResponsable.map(_ == command.event.sujetoId),
        sujetoResponsable = command.event.sujetoResponsable
      )

      persistAsync(event) { _ =>
        state += event
        if (sujetosNoResponsables.isEmpty) {
          replyTo ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
        } else {
          messageProducer.produce(
            sujetosNoResponsables.map { sujetoNoResponsable =>
              val event = command.event
              val redirection = event.copy(
                sujetoId = sujetoNoResponsable
              )
              KafkaKeyValue(redirection.aggregateRoot, serialization.encode(redirection))
            }.toSeq,
            "ObjetoReceiveSnapshot"
          ) { keyValue =>
            replyTo ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
          }
        }
      }
    }
    case command: ObjetoSnapshotPersistedReaction if !state.sujetosCotitulares.contains(command.event.sujetoId) =>
      val replyTo = sender()

      val event = CotitularidadAddedSujetoCotitular(
        command.deliveryId,
        command.event.sujetoId,
        command.objetoId,
        command.tipoObjeto,
        isResponsable = command.event.sujetoResponsable.map(_ == command.event.sujetoId),
        sujetoResponsable = command.event.sujetoResponsable
      )
      persistAsync(event) { _ =>
        state += event
        replyTo ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)
      }

    case command: ObjetoSnapshotPersistedReaction if state.sujetosCotitulares.contains(command.event.sujetoId) =>
      sender() ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

    case command: ObjetoSnapshotPersistedReaction if command.event.sujetoResponsable.isEmpty =>
      sender() ! Response.SuccessProcessing(command.aggregateRoot, command.deliveryId)

    case other => log.error(s"[$persistenceId] Unexpected message |  ${other}")

  }

  val persistenceId = self.path.name
  override def receiveRecover: Receive = {
    case evt: CotitularidadEvents =>
      state += evt
    case SnapshotOffer(_, snapshot: CotitularidadState) =>
      state = snapshot
    case r: RecoveryCompleted =>
    case unknown =>
      log.error(s"[$persistenceId] error received recover with [$unknown]")
  }

}

object CotitularidadActor extends ShardedEntity[MonitoringAndMessageProducer] {
  def props(requirements: MonitoringAndMessageProducer): Props = Props(new CotitularidadActor(requirements))
}
