package life_cycle

import scala.concurrent.ExecutionContext

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import design_principles.microservice.{Microservice, MicroserviceRequirements}
import life_cycle.typed.controller.{LivenessController, ReadinessController, ShutdownController}
import life_cycle.typed.{AppLifecycle, AppLifecycleActor}
import akka.actor.typed.scaladsl.adapter._
class AppLifecycleMicroservice(implicit microserviceRequirements: MicroserviceRequirements)
    extends Microservice[MicroserviceRequirements] {
  def route: Route = {
    val ctx = microserviceRequirements.ctx
    implicit val system: ActorSystem[Nothing] = ctx.toTyped
    //implicit val ec: ExecutionContext = microserviceRequirements.executionContext
    val monitoring = microserviceRequirements.monitoring

    val appLifecycle = new AppLifecycle(AppLifecycleActor.init(ctx.toTyped))
    val livenessController = new LivenessController(monitoring)
    val readinessController = new ReadinessController(appLifecycle, monitoring)(ctx.dispatcher)
    val shutdownController = new ShutdownController(appLifecycle, monitoring)

    val appLifeCycleRoutes =
      livenessController.route ~
      readinessController.route ~
      shutdownController.route
    appLifeCycleRoutes
  }
}
