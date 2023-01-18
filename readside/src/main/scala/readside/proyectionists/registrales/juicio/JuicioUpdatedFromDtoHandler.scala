package readside.proyectionists.registrales.juicio
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import cassandra.write.CassandraWriteProduction
import consumers.registral.juicio.domain.JuicioEvents.JuicioUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import readside.proyectionists.registrales.juicio.projections.JuicioUpdatedFromDtoProjection

class JuicioUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[JuicioUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "JuicioUpdatedFromDto"
  override def topicRetry: String = "JuicioUpdatedFromDto_retry"
  override def topicError: String = "JuicioUpdatedFromDto_error"

  import consumers.registral.juicio.infrastructure.json._

  override def processInput(input: String): Either[Throwable, JuicioUpdatedFromDto] =
    serialization
      .maybeDecode[JuicioUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()

  override def processMessage(registro: JuicioUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = JuicioUpdatedFromDtoProjection(registro)
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
