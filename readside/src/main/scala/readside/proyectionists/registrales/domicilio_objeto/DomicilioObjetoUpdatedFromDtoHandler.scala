package readside.proyectionists.registrales.domicilio_objeto
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.domicilio_objeto.projections.DomicilioObjetoUpdatedFromDtoProjection

class DomicilioObjetoUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[DomicilioObjetoUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "DomicilioObjetoUpdatedFromDto"
  override def topicRetry: String = "DomicilioObjetoUpdatedFromDto_retry"
  override def topicError: String = "DomicilioObjetoUpdatedFromDto_error"

  import consumers.registral.domicilio_objeto.infrastructure.json._

  override def processInput(input: String): Either[Throwable, DomicilioObjetoUpdatedFromDto] =
    serialization
      .maybeDecode[DomicilioObjetoUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: DomicilioObjetoUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = DomicilioObjetoUpdatedFromDtoProjection(registro)
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
