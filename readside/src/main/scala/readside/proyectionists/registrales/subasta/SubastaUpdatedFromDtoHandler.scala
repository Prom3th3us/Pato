package readside.proyectionists.registrales.subasta
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.subasta.domain.SubastaEvents.SubastaUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.subasta.projections.SubastaUpdatedFromDtoProjection

class SubastaUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[SubastaUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "SubastaUpdatedFromDto"
  override def topicError: String = "SubastaUpdatedFromDto_error"
  override def topicRetry: String = "SubastaUpdatedFromDto_retry"

  import consumers.registral.subasta.infrastructure.json._

  override def processInput(input: String): Either[Throwable, SubastaUpdatedFromDto] =
    serialization
      .maybeDecode[SubastaUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: SubastaUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = SubastaUpdatedFromDtoProjection(registro)
    projection.updateReadside()
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
