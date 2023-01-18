package consumers.no_registral.objeto.infrastructure.consumer

import scala.concurrent.Future
import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.objeto.application.entities.ObjetoCommands.ObjetoUpdateCotitulares
import consumers.no_registral.objeto.infrastructure.json._
import design_principles.actor_model.Response
import monitoring.Monitoring
import serialization.maybeDecode

case class ObjetoUpdateCotitularesTransaction(actorRef: ActorRef, monitoring: Monitoring)(
    implicit
    actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ObjetoUpdateCotitulares](monitoring) {

  def topic = "ObjetoUpdatedCotitulares"
  def topicRetry = "ObjetoUpdatedCotitulares_retry"
  def topicError = "ObjetoUpdatedCotitulares_error"

  def processInput(input: String): Either[Throwable, ObjetoUpdateCotitulares] =
    maybeDecode[ObjetoUpdateCotitulares](input)

  def processMessage(cmd: ObjetoUpdateCotitulares): Future[Response.SuccessProcessing] = {
    actorRef.ask[Response.SuccessProcessing](cmd)
  }
}
