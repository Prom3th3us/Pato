package serialization.advanced

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import play.api.libs.json._

object LocalDateTimeSerializer {

  val datePattern = "y-MM-dd HH:mm:ss.S"
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(datePattern)

  private def localDateTimeWrites: Writes[LocalDateTime] =
    (d: LocalDateTime) => {
      val clampedDate = d match {
        case _ if d.equals(LocalDateTime.MIN) =>
          LocalDateTime.of(1970, 1, 1, 0, 0)
        case _ if d.equals(LocalDateTime.MAX) =>
          LocalDateTime.of(9999, 1, 1, 0, 0)
        case _ => d
      }
      JsString(
        formatter.format(clampedDate)
      )
    }

  private def localDateTimeReads: Reads[LocalDateTime] = {
    case JsString(ss) =>
      JsSuccess(LocalDateTime.parse(ss, formatter))

    case _ =>
      JsError(
        Seq(
          JsPath ->
          Seq(JsonValidationError("error.expected.string"))
        )
      )
  }

  implicit val dateFormat: Format[LocalDateTime] =
    Format[LocalDateTime](localDateTimeReads, localDateTimeWrites)
  //implicit val dateFormatO: OFormat[LocalDateTime] = Jsonx.formatCaseClass[LocalDateTime]

}
