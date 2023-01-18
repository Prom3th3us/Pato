package serialization.advanced

import play.api.libs.json._

import scala.collection.IndexedSeq

object MapSerializer {

  object JsValueOps {
    def toJsValue: PartialFunction[Any, JsValue] = {
      case v: String => JsString(v)
      case v: Boolean => JsBoolean(v)
      case v: Int => JsNumber(v)
      case v: BigDecimal => JsNumber(v)
      case v: List[_] => JsArray(v.map { toJsValue })
      case v: IndexedSeq[_] => JsArray(v.map { toJsValue })
      case Some(any) => toJsValue(any)
      case None => JsNull
      case null => JsNull
      case v: Map[_, _] => JsObject(v.collect { case (k: String, v: Any) => k -> toJsValue(v) })
    }
    def fromJsValue: PartialFunction[JsValue, Any] = {
      case v: JsString => v.value
      case v: JsNumber => v.value
      case v: JsBoolean => v.value
      case v: JsArray => v.value.map { fromJsValue }
      case JsNull => None
      case any: JsObject => any.value
    }
  }

  implicit def read: Reads[Map[String, Any]] = {
    case JsObject(map: Map[String, JsValue]) =>
      JsSuccess(map.map {
        case (key: String, value: JsValue) => (key, JsValueOps.fromJsValue(value))
      })
    case _ =>
      JsError(
        Seq(
          JsPath ->
          Seq(JsonValidationError("error.expected.string"))
        )
      )
  }
  implicit def write: Writes[Map[String, Any]] =
    (d: Map[String, Any]) =>
      JsObject(d.map {
        case (key: String, value: Any) => (key, JsValueOps.toJsValue(value))
      })

  implicit val mapFormat: Format[Map[String, Any]] =
    Format[Map[String, Any]](read, write)

  def jsonToMap2(js: JsObject): Map[String, String] =
    Json.toJson(js).as[Map[String, List[JsValue]]].map {
      case (k: String, v: List[JsValue]) => (k, v.map(j => j.toString()).toString())
    }

  def jsonToMap(js: JsObject): Map[String, String] =
    js.keys.foldLeft(Map.empty[String, String]) {
      case (map, k) => map + (k -> (js \ k).get.toString())
    }
}
