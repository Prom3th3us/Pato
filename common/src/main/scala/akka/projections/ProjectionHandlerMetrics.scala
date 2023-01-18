package akka.projections

import akka.Done
import monitoring.{Counter, Histogram, Monitoring}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

abstract class ProjectionHandlerMetrics(
    monitoring: Monitoring
)(implicit ec: ExecutionContext) {

  final private val metricPrefix = "projection-handler"
  final private val controllerId = api.Utils.Transformation.to_underscore(this.getClass.getSimpleName)
  final protected val requests: Counter = monitoring.counter(s"$metricPrefix-$controllerId-request")
  final protected val errors: Counter = monitoring.counter(s"$metricPrefix-$controllerId-error")
  final protected val latency: Histogram = monitoring.histogram(s"$metricPrefix-$controllerId-latency")

  private final val log = LoggerFactory.getLogger(this.getClass)

  final protected def recordRequests(future: Future[Done]): Unit =
    requests.increment()
  final protected def recordLatency(future: Future[Done]): Unit =
    latency.recordFuture(future)
  final protected def recordErrors(future: Future[Done]): Unit =
    future onComplete {
      case Success(_) => ()
      case Failure(e) =>
        e match {
          case exception: Throwable =>
            errors.increment()
            log.error(exception.getMessage)
        }
    }
}
