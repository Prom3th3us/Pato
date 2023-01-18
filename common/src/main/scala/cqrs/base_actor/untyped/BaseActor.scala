package cqrs.base_actor.untyped

import akka.actor.Actor
import cqrs.untyped.command.{CommandBus, SyncCommandBus}
import cqrs.untyped.query.{QueryBus, SyncQueryBus}
import ddd.AbstractState
import design_principles.actor_model.{Command, Event, Query}
import monitoring.{Counter, Monitoring}
import org.slf4j.{Logger, LoggerFactory}
import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

abstract class BaseActor[E <: Event, State <: AbstractState[E]](monitoring: Monitoring) extends Actor {
  val name: String = utils.Inference.getSimpleName(this.getClass.getName)

  val commandsCounter: Counter = monitoring.counter(s"$name-commands")
  val queriesCounter: Counter = monitoring.counter(s"$name-queries")
  val unexpectedCounter: Counter = monitoring.counter(s"$name-unexpected")

  implicit private val ec: ExecutionContextExecutor = context.system.dispatcher
  implicit val logger: Logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  val persistenceId: String = self.path.name
  var state: State
  var lastDeliveryId: BigInt = 0

  val commandBus: CommandBus[Try] = new SyncCommandBus(logger)
  val queryBus: QueryBus[Try] = new SyncQueryBus(logger)

  def receive: Receive = {
    case cmd: Command =>
      commandsCounter.increment()
      commandBus.publish(cmd)
    case query: Query =>
      queriesCounter.increment()
      queryBus.ask(query)
    case unexpectedMsg =>
      unexpectedCounter.increment()
      logger.warn(s"[$persistenceId]Unexpected message $unexpectedMsg")
  }
  override def preStart(): Unit = {
    super.preStart()
    setupHandlers()
  }
  def setupHandlers(): Unit
}
