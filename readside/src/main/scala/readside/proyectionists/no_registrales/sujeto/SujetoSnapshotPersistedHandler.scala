package readside.proyectionists.no_registrales.sujeto
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite

import scala.concurrent.Future
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import consumers.no_registral.sujeto.domain.SujetoEvents.SujetoSnapshotPersisted
import design_principles.actor_model.Response.SuccessProcessing
import design_principles.actor_model.Response
import monitoring.Monitoring
import oracle.Oracle.connOracleReadsideToCass
import org.slf4j.LoggerFactory
import readside.proyectionists.no_registrales.sujeto.projections.SujetoSnapshotPersistedProjection

import scala.util.{Failure, Success}

class SujetoSnapshotPersistedHandler(
    implicit
    r: MonitoringAndCassandraWrite
) extends ActorTransaction[SujetoSnapshotPersisted](r.monitoring)(r.actorTransactionRequirements) {
  private val log = LoggerFactory.getLogger(this.getClass)

  override def topic: String = "SujetoSnapshotPersisted"
  override def topicRetry: String =  "SujetoSnapshotPersisted_retry"
  override def topicError: String = "SujetoSnapshotPersisted_error"

  import consumers.no_registral.sujeto.infrastructure.json._

  override def processInput(input: String): Either[Throwable, SujetoSnapshotPersisted] =
    serialization
      .maybeDecode[SujetoSnapshotPersisted](input)

  val cassandra = new CassandraWriteProduction()

  override def processMessage(registro: SujetoSnapshotPersisted): Future[Response.SuccessProcessing] = {
    //recordLag(calculateLag(registro.deliveryId.toString))
    val projection = SujetoSnapshotPersistedProjection(registro)
    for {
      done <- r.cassandraWrite.writeState(projection).andThen {
        case Failure(exception) => log.error("Dont persist sujeto " + exception )
        case Success(value) => log.debug("Persist sujeto " + value )
          //connOracleReadsideToCass(registro.deliveryId.toString(),"sujeto", registro.registro.get.SUJ_CANAL_ORIGEN.getOrElse("TAX"))
      }
    } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
  }
}
