package monitoring

import scala.concurrent.Future
import play.api.libs.json.Reads
import org.slf4j.Logger

final class KafkaConsumerHandlerError[E: Reads](monitoring: Monitoring, logger: Logger) {

  private val metricPrefix = "copernico-kafka-consumer"

  def onParsed(e: E): Future[Unit] = Future.successful {
    monitoring.counter(s"$metricPrefix-parsed").increment()
  }

  def recordLatencyParsingInMillis(before: Long, after: Long): Unit =
    monitoring.histogram(s"$metricPrefix-parsing").record(after - before)

  def onSuccess(e: E): Future[Unit] = Future.successful {
    monitoring.counter(s"$metricPrefix-success").increment()
  }

  def recordLatencyProcessingInMillis(before: Long, after: Long): Unit =
    monitoring.histogram(s"$metricPrefix-processing").record(after - before)

  def onParsingFailure(e: String): Future[Unit] = Future.successful {
    logger.error(e)
    monitoring.counter(s"$metricPrefix-parsing-failure").increment()
  }

  def onFailure(e: E): Future[Unit] = Future.successful {
    logger.error(e.toString)
    monitoring.counter(s"$metricPrefix-failure").increment()
  }

  def onDeadLetter(e: E): Future[Unit] = Future.successful {
    logger.error(e.toString)
    monitoring.counter(s"$metricPrefix-send-to-dead-letter").increment()
  }
}
