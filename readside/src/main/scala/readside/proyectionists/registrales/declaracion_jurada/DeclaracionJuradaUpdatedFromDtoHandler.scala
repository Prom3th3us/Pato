package readside.proyectionists.registrales.declaracion_jurada
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaEvents.DeclaracionJuradaUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.declaracion_jurada.projections.{
  DeclaracionJuradaProjection,
  DeclaracionJuradaUpdatedFromDtoProjection
}

class DeclaracionJuradaUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[DeclaracionJuradaUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "DeclaracionJuradaUpdatedFromDto"
  override def topicRetry: String = "DeclaracionJuradaUpdatedFromDto_retry"
  override def topicError: String = "DeclaracionJuradaUpdatedFromDto_error"

  import consumers.registral.declaracion_jurada.infrastructure.json._

  override def processInput(input: String): Either[Throwable, DeclaracionJuradaUpdatedFromDto] =
    serialization
      .maybeDecode[DeclaracionJuradaUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: DeclaracionJuradaUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = DeclaracionJuradaUpdatedFromDtoProjection(registro)
    projection.updateReadside()
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
