package akka.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import monitoring.{Counter, Histogram, Monitoring}
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsArray, JsObject, JsString}

abstract class Controller(monitoring: Monitoring) extends PlayJsonSupport {

  val log = LoggerFactory.getLogger(this.getClass)

  def route: Route
  val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e =>
      criticals.increment()
      complete((StatusCodes.InternalServerError, e.getMessage))
  }

  private val metricPrefix = "controller"
  private val controllerId = api.Utils.Transformation.to_underscore(this.getClass.getSimpleName)
  protected val warnings: Counter = monitoring.counter(s"$metricPrefix-$controllerId-warning")
  protected val criticals: Counter = monitoring.counter(s"$metricPrefix-$controllerId-critical")
  protected val requests: Counter = monitoring.counter(s"$metricPrefix-$controllerId-request")
  protected val latency: Histogram = monitoring.histogram(s"$metricPrefix-$controllerId-latency")

  def handleErrors(exceptionHandler: ExceptionHandler): Directive[Unit] =
    handleExceptions(exceptionHandler) & handleRejections(rejectionsHandler)

  private val rejectionsHandler: RejectionHandler = RejectionHandler
    .newBuilder()
    .handle {
      case ex: ValidationRejection =>
        val errorMsg = "validation rejection has occurred: " + ex.message
        complete((StatusCodes.BadRequest, JsObject(Map("errors" -> JsArray(Seq(JsString(errorMsg)))))))
      case ex: MalformedRequestContentRejection =>
        val errorMsg = "malformed request has occurred: " + ex.message
        complete((StatusCodes.BadRequest, JsObject(Map("errors" -> JsArray(Seq(JsString(errorMsg)))))))
    }
    .result()
}
