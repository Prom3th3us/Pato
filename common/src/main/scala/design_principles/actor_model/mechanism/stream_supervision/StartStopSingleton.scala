package design_principles.actor_model.mechanism.stream_supervision

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.entity.ShardedEntity.NoRequirements
import akka.entity.SingletonEntity

import scala.collection.mutable
import scala.concurrent.duration.DurationInt

object StartStopSingleton extends SingletonEntity[NoRequirements] {
  case class SubscribeMe(topic: String, actorRef: ActorRef)
  case class Start()
  case class Stop()
  case class StartByTopic(topic: String)
  case class StopByTopic(topic: String)
  case class Ping()
  case class Pong()

  override def props(requirements: NoRequirements): Props = Props(new StartStopSingleton())
  def start(implicit actorSystem: ActorSystem): ActorRef = startWithRequirements(NoRequirements())
}

class StartStopSingleton() extends Actor with ActorLogging {

  import StartStopSingleton._

  val phonebook: mutable.Map[String, ActorRef] = mutable.Map.empty

  override def receive: Receive = {

    case SubscribeMe(topic: String, actorRef: ActorRef) =>
      phonebook(topic) = actorRef
      log.info(s"StartStopSingleton added ${topic} to the list to be stopped/started")

    case message @ StartByTopic(topic) if phonebook.keys.toSet.contains(topic) =>
      phonebook(topic) ! message

    case message @ StartByTopic(topic) if !phonebook.keys.toSet.contains(topic) =>
      log.error(s"You are trying to start a topic ($topic) who does not exist in the codebase")
    case message @ StopByTopic(topic) if phonebook.keys.toSet.contains(topic) =>
      phonebook(topic) ! message
    case message @ StopByTopic(topic) if !phonebook.keys.toSet.contains(topic) =>
      log.error(s"You are trying to start a topic ($topic) who does not exist in the codebase")

    case Start() =>
      phonebook.values foreach { _ ! Start() }
    case Stop() =>
      phonebook.values foreach { _ ! Stop() }

    case Pong() => ()

  }

  object Call extends Runnable {
    def run(): Unit = {
      phonebook.values foreach { _ ! Ping() }
    }
  }

  context.system.scheduler.scheduleOnce(30 seconds, Call)(context.system.dispatcher)

}
