package design_principles.actor_model.context_provider

import akka.actor.ActorSystem
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.typed.{Cluster, Subscribe}
import akka.http.AkkaHttpServer.StopAkkaHttpServer
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Guardian {
  def getContext(
      requirements: GuardianRequirements
  ): ActorSystem = {

    val system = ActorSystem(
      requirements.actorSystemName,
      requirements.config
    )
    AkkaManagement(system).start()
    ClusterBootstrap(system).start()

    system

  }
}
