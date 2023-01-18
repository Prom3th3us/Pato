package readside.proyectionists.registrales.parametrica_recargo
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.parametrica_recargo.projections.ParametricaRecargoUpdatedFromDtoProjection

class ParametricaRecargoUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[ParametricaRecargoUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "ParametricaRecargoUpdatedFromDto"
  override def topicRetry: String = "ParametricaRecargoUpdatedFromDto_retry"
  override def topicError: String = "ParametricaRecargoUpdatedFromDto_error"

  import consumers.registral.parametrica_recargo.infrastructure.json._

  override def processInput(input: String): Either[Throwable, ParametricaRecargoUpdatedFromDto] =
    serialization
      .maybeDecode[ParametricaRecargoUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: ParametricaRecargoUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = ParametricaRecargoUpdatedFromDtoProjection(registro)
    projection.updateReadside()
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
