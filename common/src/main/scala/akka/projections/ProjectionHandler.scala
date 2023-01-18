package akka.projections

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.projection.eventsourced.EventEnvelope
import akka.projection.scaladsl.Handler

import scala.concurrent.Future

abstract class ProjectionHandler[T](val settings: ProjectionSettings, system: ActorSystem[_])
    extends ProjectionHandlerMetrics(settings.monitoring)(system.toClassic.dispatcher)
    with Handler[EventEnvelope[T]] {

  def processMessage(envelope: EventEnvelope[T]): Future[Done]

  final override def process(envelope: EventEnvelope[T]): Future[Done] = {
    val future = processMessage(envelope)
    recordLatency(future)
    recordRequests(future)
    recordErrors(future)
    future
  }
}
