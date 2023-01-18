package readside.proyectionists.registrales.plan_pago
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.plan_pago.domain.PlanPagoEvents.PlanPagoUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.plan_pago.projections.PlanPagoUpdatedFromDtoProjection

class PlanPagoUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[PlanPagoUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "PlanPagoUpdatedFromDto"
  override def topicRetry: String = "PlanPagoUpdatedFromDto_retry"
  override def topicError: String = "PlanPagoUpdatedFromDto_error"

  import consumers.registral.plan_pago.infrastructure.json._

  override def processInput(input: String): Either[Throwable, PlanPagoUpdatedFromDto] =
    serialization
      .maybeDecode[PlanPagoUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: PlanPagoUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = PlanPagoUpdatedFromDtoProjection(registro)
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
