package api.actor_transaction

import akka.pattern.AskTimeoutException
import com.datastax.oss.driver.api.core.DriverTimeoutException
import ddd.ExternalDto
import design_principles.actor_model.Response
import monitoring.{Counter, Histogram, Monitoring}
import org.slf4j.LoggerFactory
import serialization.{SerializationError, maybeDecode}

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

abstract class ActorTransactionMetrics(
    monitoring: Monitoring
)(implicit ec: ExecutionContext) {

  final private val metricPrefix = "actor-transaction"
  final private val controllerId = api.Utils.Transformation.to_underscore(this.getClass.getSimpleName)
  final protected val requests: Counter = monitoring.counter(s"$metricPrefix-$controllerId-request")
  final protected val errors: Counter = monitoring.counter(s"$metricPrefix-$controllerId-error")
  final protected val errorsATO: Counter = monitoring.counter(s"$metricPrefix-$controllerId-error-asktimeout")
  final protected val latency: Histogram = monitoring.histogram(s"$metricPrefix-$controllerId-latency")
  final protected val lag: Histogram = monitoring.histogram(s"$metricPrefix-$controllerId-lag")

  private final val log = LoggerFactory.getLogger(this.getClass)

  final protected def recordRequests(): Unit =
    requests.increment()

  final protected def recordLag(n: Long): Unit =
    lag.record(n)

  final protected def recordLatency(future: Future[Response.SuccessProcessing]): Unit =
    latency.recordFuture(future)
  final protected def recordErrors(throwable: Throwable, input: String): Unit = {

    val trimmedList: List[String] = input.split("\"").map(_.trim).toList
    val ev_id = trimmedList(3)
    val a = trimmedList.indexOf("BOB_SUJ_IDENTIFICADOR")

    val suj_iden = trimmedList(a + 1) match {
      case _ if a.equals(-1) => ""
      case _ => trimmedList(a + 2)
    }

    throwable match {
      case e: SerializationError =>
        errors.increment()
        log.error(e.getMessage)
      case e: AskTimeoutException =>
        errors.increment()
        errorsATO.increment()
        log.error(e.getMessage + s" [${ev_id}  -  ${suj_iden}]")
      case unexpectedException: Throwable =>
        errors.increment()
        log.error(unexpectedException.getMessage)
    }
  }

  private def toLocalDateTime(num: String) = {
     val fechaString =
       s"${num(0)}${num(1)}${num(2)}${num(3)}-${num(4)}${num(5)}-${num(6)}${num(7)}T${num(8)}${num(9)}:${num(10)}${
         num(
           11
         )
       }:${num(12)}${num(13)}.${num(14)}${num(15)}${num(16)}"
     val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
     LocalDateTime.parse(fechaString, formatter)
  }

  protected def calculateLag(evId: String) = {
     //time in the source
     //GMT -3
     val ti: LocalDateTime = toLocalDateTime(evId)
     //time in the sink
     //GMT -3
     val tf: LocalDateTime = ZonedDateTime.now(ZoneId.of("UTC-3")).toLocalDateTime
     //difference
     ChronoUnit.MILLIS.between(ti, tf)
  }

}
