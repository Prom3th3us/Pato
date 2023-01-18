package readside.proyectionists.registrales.etapas_procesales
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.registral.etapas_procesales.domain.EtapasProcesalesEvents.EtapasProcesalesUpdatedFromDto
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import readside.proyectionists.registrales.etapas_procesales.projections.EtapasProcesalesUpdatedFromDtoProjection

class EtapasProcesalesUpdatedFromDtoHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[EtapasProcesalesUpdatedFromDto](r.monitoring)(r.actorTransactionRequirements) {

  override def topic: String = "EtapasProcesalesUpdatedFromDto"
  override def topicRetry: String = "EtapasProcesalesUpdatedFromDto_retry"
  override def topicError: String = "EtapasProcesalesUpdatedFromDto_error"

  import consumers.registral.etapas_procesales.infrastructure.json._

  override def processInput(input: String): Either[Throwable, EtapasProcesalesUpdatedFromDto] =
    serialization
      .maybeDecode[EtapasProcesalesUpdatedFromDto](input)

  val cassandra = new CassandraWriteProduction()
  override def processMessage(registro: EtapasProcesalesUpdatedFromDto): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = EtapasProcesalesUpdatedFromDtoProjection(registro)
    for {
      done <- cassandra writeState projection
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }

}
