package readside.proyectionists.no_registrales.objeto

import akka.entity.ShardedEntity.MonitoringAndCassandraWrite
import api.actor_transaction.ActorTransaction
import cassandra.write.CassandraWriteProduction
import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted
import design_principles.actor_model.Response
import design_principles.actor_model.Response.SuccessProcessing
import oracle.Oracle.connOracleReadsideToCass
import org.slf4j.LoggerFactory
import readside.proyectionists.no_registrales.objeto.projections.ObjetoSnapshotPersistedProjection

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ObjetoSnapshotPersistedHandler(
                                      implicit
                                      r: MonitoringAndCassandraWrite
                                    ) extends ActorTransaction[ObjetoSnapshotPersisted](r.monitoring)(r.actorTransactionRequirements) {

  private val log = LoggerFactory.getLogger(this.getClass)

  override def topic: String = "ObjetoSnapshotPersistedReadside"

  override def topicRetry: String = "ObjetoSnapshotPersistedReadside_retry"

  override def topicError: String = "ObjetoSnapshotPersistedReadside_error"

  override def processInput(input: String): Either[Throwable, ObjetoSnapshotPersisted] = {
    import consumers.no_registral.objeto.infrastructure.json._
    serialization
      .maybeDecode[ObjetoSnapshotPersisted](input)
  }

  override def processMessage(registro: ObjetoSnapshotPersisted): Future[Response.SuccessProcessing] = {
    // recordLag(calculateLag(registro.deliveryId.toString))
    val projection = ObjetoSnapshotPersistedProjection(registro)
    if (registro.operacion.equals("U")) {
      for {
        done <- r.cassandraWrite.writeState(projection).andThen {
          case Failure(exception) => log.debug("Dont persist objeto" + exception )
          case Success(value) => log.debug("Persist objeto" + value )
            //connOracleReadsideToCass(registro.deliveryId.toString(),"objeto", registro.registro.get.SOJ_CANAL_ORIGEN.getOrElse("TAX"))
        }
      } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
    } else if (registro.operacion.equals("FD")) {
      val cassandra = new CassandraWriteProduction()
      for {
        done <- cassandra
          .cql(
            s"""
          DELETE FROM read_side.buc_sujeto_objeto """ +
              """ WHERE soj_suj_identificador = """ +
              s""" '${registro.sujetoId}' """ +
              s""" and soj_tipo_objeto = '${registro.tipoObjeto}' """ +
              s""" and soj_identificador = '${registro.objetoId}' """
          )
          .recover { ex: Throwable =>
            log.error(ex.getMessage)
            ex
          }
      } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
    } else {
      val cassandra = new CassandraWriteProduction()
      for {
        done <- cassandra
          .cql(
            s"""
          DELETE FROM read_side.buc_obligaciones """ +
              """ WHERE bob_suj_identificador = """ +
              s""" '${registro.sujetoId}' """ +
              s""" and bob_soj_tipo_objeto = '${registro.tipoObjeto}' """ +
              s""" and bob_soj_identificador = '${registro.objetoId}' """
          )
          .recover { ex: Throwable =>
            log.error(ex.getMessage)
            ex
          }
      } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
    }
  }
}
