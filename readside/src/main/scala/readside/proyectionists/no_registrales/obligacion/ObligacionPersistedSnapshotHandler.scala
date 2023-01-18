package readside.proyectionists.no_registrales.obligacion

import akka.entity.ShardedEntity.MonitoringAndCassandraWrite
import api.actor_transaction.ActorTransaction
import cassandra.write.CassandraWriteProduction
import consumers.no_registral.obligacion.domain.ObligacionEvents.ObligacionPersistedSnapshot
import design_principles.actor_model.Response
import design_principles.actor_model.Response.SuccessProcessing
import oracle.Oracle.connOracleReadsideToCass
import org.slf4j.LoggerFactory
import readside.proyectionists.no_registrales.obligacion.projectionists.ObligacionSnapshotProjection

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ObligacionPersistedSnapshotHandler(
                                          implicit
                                          r: MonitoringAndCassandraWrite
                                        ) extends ActorTransaction[ObligacionPersistedSnapshot](r.monitoring)(r.actorTransactionRequirements) {

  private val log = LoggerFactory.getLogger(this.getClass)

  override def topic: String = "ObligacionPersistedSnapshot"

  override def topicRetry: String = "ObligacionPersistedSnapshot_retry"

  override def topicError: String = "ObligacionPersistedSnapshot_error"

  import consumers.no_registral.obligacion.infrastructure.json._

  override def processInput(input: String): Either[Throwable, ObligacionPersistedSnapshot] =
    serialization
      .maybeDecode[ObligacionPersistedSnapshot](input)

  override def processMessage(registro: ObligacionPersistedSnapshot): Future[Response.SuccessProcessing] = {
    //log.error("llego event")
    //recordLag(calculateLag(registro.deliveryId.toString))
    if (registro.operacion.equals("U")) {
      val projection = ObligacionSnapshotProjection(registro)

      /*r.cassandraWrite.writeState(projection).onComplete {
        case Failure(exception) => log.error(exception)

        case Success(value) =>

          connOracleReadsideToCass(registro.sujetoId,registro.tipoObjeto,registro.objetoId,registro.obligacionId)
          SuccessProcessing(registro.aggregateRoot, registro.deliveryId)

      }*/

      for {
        done <- r.cassandraWrite.writeState(projection).andThen {
          case Failure(exception) => log.debug("Dont persist obligacion" + exception )
          case Success(value) => {
            //log.error("ERROR - 1 " + registro.deliveryId)
            log.debug("Persist obligacion" + value)
            //connOracleReadsideToCass(registro.deliveryId.toString(), "obligacion", registro.registro.get.BOB_CANAL_ORIGEN.getOrElse("TAX"))
          }
        }


        /*recover { ex: Throwable =>
        connOracleReadsideToCass(registro.sujetoId,registro.tipoObjeto,registro.objetoId,registro.obligacionId)
        log.error(ex.getMessage)
        log.error("readside oracle")
        ex
      }*/
      } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
    } else {
      val cassandra = new CassandraWriteProduction()
      val bco = registro.registro match {
        case Some(x) => x.BOB_CANAL_ORIGEN.getOrElse("TAX")
        case None => "TAX"
      }
      for {
        done <- cassandra
          .cql(
            s"""
          DELETE FROM read_side.buc_obligaciones """ +
              """ WHERE bob_suj_identificador = """ +
              s""" '${registro.sujetoId}' """ +
              s""" and bob_soj_tipo_objeto = '${registro.tipoObjeto}' """ +
              s""" and bob_soj_identificador = '${registro.objetoId}' """ +
              s""" and bob_obn_id = '${registro.obligacionId}' """
          )
          .andThen {
            case Failure(exception) => log.debug("Dont persist obligacion" + exception )
            case Success(_) => {
              //log.error("ERROR - -1 " + registro.deliveryId)
              log.debug("Persiste Obligacion")
              //connOracleReadsideToCass(registro.deliveryId.toString(), "obligacion", bco)
            }
          }
      } yield SuccessProcessing(registro.aggregateRoot, registro.deliveryId)
    }
  }

}
