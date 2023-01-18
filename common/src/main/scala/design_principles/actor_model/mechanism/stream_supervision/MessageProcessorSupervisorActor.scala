package design_principles.actor_model.mechanism.stream_supervision

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.KillSwitch
import api.actor_transaction.ActorTransactionController
import design_principles.actor_model.mechanism.stream_supervision.StartStopSingleton.{
  Ping,
  Pong,
  Start,
  StartByTopic,
  Stop,
  StopByTopic,
  SubscribeMe
}

import scala.collection.mutable

class MessageProcessorSupervisorActor(
    startStopSingleton: ActorRef,
    streams: Map[String, ActorTransactionController]
) extends Actor
    with ActorLogging {

  var killSwitches: mutable.Map[String, KillSwitch] = mutable.Map.empty

  override def preStart(): Unit = {
    super.preStart()
    streams.foreach {
      case (topic, controller) =>
        startStopSingleton ! SubscribeMe(
          topic,
          self
        )
    }
  }

  override def receive: Receive = {

    case Ping() => sender() ! Pong()

    case StartByTopic(topic) =>
      if (killSwitches.contains(topic)) {
        log.debug(s"Already started!")
      } else {
        streams(topic).startTransaction() foreach { killSwitch =>
          killSwitches(topic) = killSwitch
        }
        log.debug(s"Starting all kafka consumers on node")
      }

    case StopByTopic(topic) =>
      if (!killSwitches.contains(topic)) {
        log.debug(s"There are no kafka consumers to stop on node")
      } else {
        log.debug(s"Stopping all kafka consumers on node ")
        killSwitches(topic).shutdown
        killSwitches.remove(topic)
      }

    case Start() =>
      val inKillSwitches = (topic: String) => killSwitches.keys.toSet.contains(topic)
      val alreadyStarted = (topic: String) => inKillSwitches(topic)

      if (streams.keys.forall(alreadyStarted)) {
        log.debug(s"All kafka consumers were already started")

      } else {
        killSwitches = mutable.Map(streams.flatMap {
          case (topic, stream) if !alreadyStarted(topic) =>
            val killswitch = stream.startTransaction()
            killswitch.map(
              (topic, _)
            )
        }.toSeq: _*)
        log.debug(s"Starting all kafka consumers on node")
      }

    case Stop() =>
      if (killSwitches.isEmpty) {
        log.debug(s"There are no kafka consumers to stop on node")
      } else {
        log.debug(s"Stopping all kafka consumers on node ")
        killSwitches.values foreach (_.shutdown)
        killSwitches = killSwitches.empty
      }
  }
}
