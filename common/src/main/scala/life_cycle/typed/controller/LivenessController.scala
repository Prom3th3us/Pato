package life_cycle.typed.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.{scaladsl, Controller}
import monitoring.Monitoring
import play.api.libs.json.{JsObject, JsString}

private[life_cycle] final class LivenessController(monitoring: Monitoring) extends Controller(monitoring) {

  override val exceptionHandler = scaladsl.server.ExceptionHandler {
    case _ =>
      criticals.increment()
      complete(StatusCodes.InternalServerError)
  }

  val route: Route = get {
    path("api" / "system" / "status") {
      requests.increment()
      handleErrors(exceptionHandler) {
        complete(JsObject(Map("status" -> JsString("ok"))))
      }
    }
  }
}
