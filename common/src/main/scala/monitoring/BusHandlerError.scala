package monitoring

import org.slf4j.Logger

final class BusHandlerError(monitoring: Monitoring, logger: Logger) {
  private val metricPrefix = "copernico"

  def onSuccessCommand(commandId: String): Unit =
    monitoring.counter(s"$metricPrefix-command-success-$commandId").increment()

  def recordLatencyInMillisCommand(commandId: String, before: Long, after: Long): Unit =
    monitoring.histogram(s"$metricPrefix-command-histogram-$commandId").record(after - before)

  def onFailureCommand[E <: Throwable](error: E): Unit = {
    logger.error(error.getMessage)
    monitoring.counter(s"$metricPrefix-command-failure-${formatError(error)}").increment()
  }

  def onSuccessQuery(queryId: String): Unit =
    monitoring.counter(s"$metricPrefix-query-success-$queryId").increment()

  def recordLatencyInMillisQuery(queryId: String, before: Long, after: Long): Unit =
    monitoring.histogram(s"$metricPrefix-query-histogram-$queryId").record(after - before)

  def onFailureQuery[E <: Throwable](error: E): Unit = {
    logger.error(error.getMessage)
    monitoring.counter(s"$metricPrefix-query-failure-${formatError(error)}").increment()
  }

  def onSuccessEvent(eventId: String): Unit =
    monitoring.counter(s"$metricPrefix-event-success-$eventId").increment()

  def recordLatencyInMillisEvent(eventId: String, before: Long, after: Long): Unit =
    monitoring.histogram(s"$metricPrefix-event-histogram-$eventId").record(after - before)

  def onFailureEvent[E <: Throwable](error: E): Unit = {
    logger.error(error.getMessage)
    monitoring.counter(s"$metricPrefix-event-failure-${formatError(error)}").increment()
  }

  private def formatError[E <: Throwable](e: E): String =
    e.getClass.getCanonicalName
      .split("\\.")
      .toList
      .takeRight(2)
      .map(api.Utils.Transformation.to_underscore)
      .mkString("-")

  private def errorValues[E <: Throwable](e: E): Map[String, String] =
    e.getClass.getDeclaredFields.map { f =>
      f.setAccessible(true)
      val res = (f.getName, f.get(e).toString)
      f.setAccessible(false)
      res
    }.toMap
}
