package cqrs.base_actor.untyped

import akka.actor.ActorLogging
import akka.persistence.{
  PersistentActor,
  Recovery,
  RecoveryCompleted,
  SaveSnapshotFailure,
  SaveSnapshotSuccess,
  SnapshotOffer
}
import cqrs.untyped.event.{EventBus, SyncEventBus}
import ddd.AbstractState
import design_principles.actor_model.{Command, Event, Query}
import monitoring.{Counter, Monitoring}

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag
import scala.util.Try

abstract class PersistentBaseActor[E <: Event: ClassTag, State <: AbstractState[E]: ClassTag](monitoring: Monitoring)
    extends BaseActor[E, State](monitoring)
    with PersistentActor
    with ActorLogging {

  val persistedCounter: Counter = monitoring.counter(s"$name-persisted")

  val eventBus: EventBus[Try] = new SyncEventBus(logger)

  override def receive: Receive = super[PersistentActor].receive

  override def receiveCommand: Receive = {
    case cmd: Command =>
      commandBus.publish(cmd)
    case query: Query =>
      queryBus.ask(query)
    case other =>
      println(s"[PEPE] [$persistenceId]Unexpected message $other")
      logger.warn(s"[PEPE]  [$persistenceId]Unexpected message $other")
  }

  override def receiveRecover: Receive = {
    case e: E =>
      (state + e) match {
        case s: State => {
          state = s
        }
        case e =>
          throw new Exception(
            "Unexpectedly an AbstractState[Event] returned a different AbstractState of the same Event"
          )
      }
    case s: State =>
    case SnapshotOffer(_, snapshot: State) =>
      state = snapshot

    case SaveSnapshotSuccess =>
      logger.debug(s"RecoveryCompleted for entity [$name | $persistenceId]")

    case SaveSnapshotFailure =>
      logger.debug(s"SaveSnapshotFailure for entity [$name | $persistenceId]")

    case RecoveryCompleted =>
      logger.debug(s"RecoveryCompleted for entity [$name | $persistenceId]")

    case other =>
      println(s"[PEPE]  Unexpected event $other happened for [$name | $persistenceId]")
      logger.warn(s"[PEPE] Unexpected event $other happened for [$name | $persistenceId]")
  }

  implicit val ec: ExecutionContext = context.system.dispatcher

  def persistEvent(event: E, tags: Set[String] = Set.empty)(handler: () => Unit = () => ()): Unit = {
    //todo review the use of persistAsync
    persistAsync(event) { _ =>
      logger.debug(s"[$persistenceId] Persist event | $event")
      persistedCounter.increment()
      monitoring.counter(s"$name-persisted-${utils.Inference.getSimpleName(event.getClass.getName)}").increment()
      handler()
    }
  }

  def snapshotState(snapshot: State) = {
    saveSnapshot(snapshot)
  }
}
