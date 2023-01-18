package api.actor_transaction

import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.Config
import design_principles.actor_model.Response
import kafka.KafkaMessageProcessorRequirements
import monitoring.Monitoring

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

abstract class ActorTransaction[ExternalDto](
    monitoring: Monitoring
)(implicit actorTransactionRequirements: ActorTransactionRequirements)
    extends ActorTransactionMetrics(monitoring)(actorTransactionRequirements.executionContext) {

  def topic: String
  def topicRetry: String
  def topicError: String

  def processMessage(registro: ExternalDto): Future[Response.SuccessProcessing]

  final def transaction(input: String): Future[Response.SuccessProcessing] = {
    recordRequests()
    processInput(input) match {
      case Left(serializationError) =>
        recordErrors(serializationError, input)
        Future.failed(serializationError)

      case Right(value) =>
        val future = processMessage(value)
        future.onComplete {
          case Failure(exception) => recordErrors(exception, input)
          case Success(_) => ()
        }(actorTransactionRequirements.executionContext)
        recordLatency(future)
        future
    }
  }

  def processInput(input: String): Either[Throwable, ExternalDto]

  def controller(implicit requirements: KafkaMessageProcessorRequirements) =
    new ActorTransactionController(this, requirements)
  final def route(implicit system: akka.actor.ActorSystem, requirements: KafkaMessageProcessorRequirements): Route =
    controller.route

  protected val simpleName = utils.Inference.getSimpleName(this.getClass.getName)

  implicit val ex = actorTransactionRequirements.executionContext

}

object ActorTransaction {
  case class ActorTransactionRequirements(config: Config, executionContext: ExecutionContext)
}
