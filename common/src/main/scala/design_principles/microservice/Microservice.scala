package design_principles.microservice

import akka.http.scaladsl.server.Route

abstract class Microservice[Requirements <: MicroserviceRequirements](implicit context: Requirements) {
  def route: Route
}
