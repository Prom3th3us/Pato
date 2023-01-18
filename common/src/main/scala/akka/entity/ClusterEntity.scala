package akka.entity

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.ExecutionContext

trait ClusterEntity[Requirements] {

  val typeName: String = utils.Inference.getSimpleName(this.getClass.getName)

  def props(requirements: Requirements): Props

  def startWithRequirements(requirements: Requirements)(
      implicit
      system: ActorSystem
  ): ActorRef
}
