package consumers.no_registral.cotitularidad.infrastructure.kafka

import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.cotitularidad.application.entities.CotitularidadCommands.ObjetoSnapshotPersistedReaction
import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.Response
import monitoring.Monitoring
import serialization.maybeDecode

import scala.concurrent.Future

case class ObjetoSnapshotPersistedHandler(actorRef: ActorRef, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ObjetoSnapshotPersisted](monitoring) {

  def topic = "ObjetoSnapshotPersisted"
  def topicRetry = "ObjetoSnapshotPersisted_retry"
  def topicError = "ObjetoSnapshotPersisted_error"

  def processInput(input: String): Either[Throwable, ObjetoSnapshotPersisted] =
    maybeDecode[ObjetoSnapshotPersisted](input)

  def processMessage(evt: ObjetoSnapshotPersisted): Future[Response.SuccessProcessing] = {
    actorRef ! ObjetoSnapshotPersistedReaction(
      evt.deliveryId,
      evt.objetoId,
      evt.tipoObjeto,
      evt
    )
    Future(Response.SuccessProcessing(evt.aggregateRoot, evt.deliveryId))
  }
  /* Modified to change ask by tell to improve performance
       def processMessage(evt: ObjetoSnapshotPersisted): Future[Response.SuccessProcessing] = {
    actorRef.ask[Response.SuccessProcessing](
      ObjetoSnapshotPersistedReaction(
        evt.deliveryId,
        evt.objetoId,
        evt.tipoObjeto,
        evt
      )
    )
  }*/
}
