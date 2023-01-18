package utils.implicits

object RowT {

  import com.datastax.oss.driver.api.core.cql.Row
  implicit class RowToMap(row: Row) {
    def toMap: Map[String, String] =
      row.getFormattedContents.tail.reverse.tail.reverse
        .replace("""",""", "comma_separator")
        .split(",")
        .map(_.trim)
        .map { tuple =>
          val key = tuple.split(":").head
          val value = tuple.split(":").tail.mkString(":")
          key -> value
        }
        .toMap
  }

}
