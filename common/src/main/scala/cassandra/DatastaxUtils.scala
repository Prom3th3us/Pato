package cassandra

import java.time.{LocalDateTime, ZoneOffset}

object DatastaxUtils {
  // Akka used Datastax 3.7, not the 4.0 version which uses Instant to represent Timestamps
  // README https://docs.datastax.com/en/developer/java-driver/3.7/manual/
  def toTimestamp(date: LocalDateTime) =
    new java.util.Date(date.toInstant(ZoneOffset.UTC).getEpochSecond)
}
