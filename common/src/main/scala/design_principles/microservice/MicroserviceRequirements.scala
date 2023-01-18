package design_principles.microservice

import akka.actor.ActorSystem
import monitoring.Monitoring

trait MicroserviceRequirements {
  def monitoring: Monitoring
  def ctx: ActorSystem
}
