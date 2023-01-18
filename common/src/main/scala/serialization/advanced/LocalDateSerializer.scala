package serialization.advanced

import java.time.LocalDate

import play.api.libs.json.{Format, JsResult, JsValue, Json}

object LocalDateSerializer {

  /*
  If representing the LocalDate as an ISO date string
  (e.g. "2016-07-09") is sufficient, then the formatter becomes quite simple:
   */
  implicit val localDateFormat: Format[LocalDate] = new Format[LocalDate] {
    override def reads(json: JsValue): JsResult[LocalDate] =
      json.validate[String].map(LocalDate.parse)

    override def writes(o: LocalDate): JsValue = Json.toJson(o.toString)
  }
}
