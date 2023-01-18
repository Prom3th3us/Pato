package api.actor_transaction

import akka.http.Controller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, path, _}
import akka.http.scaladsl.server.Route
import akka.stream.KillSwitch
import kafka.{KafkaCommittablePartitionedMessageProcessor, KafkaMessageProcessorRequirements}

import scala.concurrent.ExecutionContextExecutor

class ActorTransactionController(
    actorTransaction: ActorTransaction[_],
    requirements: KafkaMessageProcessorRequirements
) extends Controller(requirements.monitoring) {

  implicit val system = requirements.system
  //implicit private val ec: ExecutionContextExecutor = system.dispatcher
  implicit private val ec: ExecutionContextExecutor = system.dispatchers.lookup("my-dispatcher")

  var currentTransaction: Option[KillSwitch] = None
  var shouldBeRunning: Boolean = false

  def stopTransaction(): Unit = {
    currentTransaction = currentTransaction match {
      case Some(killswitch) =>
        log.debug(s"${actorTransaction.topic} transaction stopped.")
        killswitch.shutdown()
        log.debug("Setting currentTransaction to None")

        None
      case None =>
        log.debug(s"${actorTransaction.topic} transaction was already stopped!")
        None
    }
  }

  def startTransaction(): Option[KillSwitch] = {
    def topic = actorTransaction.topic
    def topicRetry= actorTransaction.topicRetry
    def topicError= actorTransaction.topicError

    val transaction = actorTransaction.transaction _
    println(s"[PEPE] Starting ${actorTransaction.topic} transaction")
    log.debug(s"[PEPE] Starting ${actorTransaction.topic} transaction")

    //TODO changed to KafkaCommittablePartitionedMessageProcessor

    val (killSwitch, done) = new KafkaCommittablePartitionedMessageProcessor(requirements)
    //  val (killSwitch, done) = new KafkaTransactionalMessageProcessor(requirements)
    // val (killSwitch, done) = new KafkaCommitableMessageProcessor(requirements)
    //  val (killSwitch, done) = new KafkaCommittableSourceMessageProcessor(requirements)
    // val (killSwitch, done) = new KafkaPlainConsumerMessageProcessor(requirements)
      .run(topic, s"${topic}SINK",topicRetry,topicError, message => {
        transaction(message).map { output =>
          Seq(output.toString)
        }
      })

    done.onComplete { result =>
      // restart if the flag is set to true
      if (shouldBeRunning) {
        log.debug(s"Transaction finished with $result. Restarting it.")
        stopTransaction()
        startTransaction()
      }
    }
    log.debug("Setting currentTransaction to Some(killswitch)")
    currentTransaction = killSwitch
    killSwitch
  }
  def route: Route =
    path("api" / "system" / "health" / "topic" / actorTransaction.topic) {

      complete(StatusCodes.OK)
    }
}
