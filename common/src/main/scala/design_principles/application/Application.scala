package design_principles.application

import akka.actor.typed.ActorSystem
import akka.cluster.ClusterEvent
import com.typesafe.config.{Config, ConfigFactory}
import design_principles.microservice.Microservice

trait Application[Specialization <: design_principles.microservice.MicroserviceRequirements, M <: Microservice[
  Specialization
]] {

  def startMicroservices(
      microservices: Seq[M],
      ip: String,
      port: Int,
      actorSystemName: String,
      extraConfigurations: Config = ConfigFactory.empty
  ): ActorSystem[ClusterEvent.MemberUp]

}
