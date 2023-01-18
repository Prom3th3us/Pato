package life_cycle.typed

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import life_cycle.typed.AppLifecycleActor.{IsAppReady, Shutdown}

import scala.concurrent.Future
import scala.concurrent.duration._

private[life_cycle] class AppLifecycle(actor: ActorRef[AppLifecycleActor.Command])(implicit system: ActorSystem[_]) {
  implicit private val akkaAskTimeout: Timeout = 5.seconds
  def shutdown(): Unit = actor ! Shutdown
  def isAppReady(): Future[Boolean] = actor.ask[Boolean](IsAppReady.apply)
}
