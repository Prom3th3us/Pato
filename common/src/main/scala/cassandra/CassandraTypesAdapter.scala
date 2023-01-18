package cassandra

import java.time.{LocalDate, LocalDateTime}
import java.util

import play.api.libs.json.JsObject

import scala.jdk.CollectionConverters._

object CassandraTypesAdapter {
  def boolean(s: Boolean): java.lang.Boolean = Boolean.box(s)
  def boolean(s: Option[Boolean]): java.lang.Boolean = (s map boolean) orNull
  def int(s: BigInt): java.lang.Integer = Int box s.toInt
  def int(s: Option[BigInt]): java.lang.Integer = (s map int) orNull // TODO cast BigInt -> Int?
  def float(s: BigDecimal): java.lang.Float = Float.box(s.toFloat)
  def float(s: Option[BigDecimal]): java.lang.Float = (s map float) orNull
  def mapJson(s: JsObject): util.Map[String, String] = map(serialization.advanced.MapSerializer.jsonToMap(s))
  def mapJson(s: Option[JsObject]): util.Map[String, String] = (s.map(mapJson)) orNull
  def map(s: Map[String, String]): util.Map[String, String] = s.asJava
  def map(s: Option[Map[String, String]]): util.Map[String, String] = s.getOrElse(Map.empty).asJava
  def set(s: Set[String]): util.Set[String] = s.asJava
  def set(s: Option[Set[String]]): util.Set[String] = (s map set) orNull
  def text(s: String): String = s
  def text(s: Option[String]): String = s.orNull
  def localDateTime(s: LocalDateTime): LocalDate = s.toLocalDate
  def localDateTime(s: Option[LocalDateTime]): LocalDate = (s map localDateTime) orNull
  def localDate(s: LocalDate): LocalDate = s
  def localDate(s: Option[LocalDate]): LocalDate = s orNull

  def curate(value: Any): Object =
    value match {
      case v: Boolean => boolean(v)
      case Some(v: Boolean) => boolean(v)

      case v: Int => int(v)
      case Some(v: Int) => int(v)

      case v: BigInt => int(v)
      case Some(v: BigInt) => int(v)

      case v: BigDecimal => float(v)
      case Some(v: BigDecimal) => float(v)

      case v: JsObject => mapJson(v)
      case Some(v: JsObject) => mapJson(v)

      case v: Map[_, _] if v.nonEmpty && v.values.head.isInstanceOf[String] && v.keys.head.isInstanceOf[String] =>
        map(v.asInstanceOf[Map[String, String]])
      case Some(v: Map[_, _]) if v.nonEmpty && v.values.head.isInstanceOf[String] && v.keys.head.isInstanceOf[String] =>
        map(Some(v.asInstanceOf[Map[String, String]]))

      case v: String => text(v)
      case Some(v: String) => text(v)

      case v: LocalDateTime => localDateTime(v)
      case Some(v: LocalDateTime) => localDateTime(v)

      case v: LocalDate => localDate(v)
      case Some(v: LocalDate) => localDate(v)

      case _ => null

    }

  def curateKeyValueList(bindings: List[(String, Any)]): List[(String, Object)] =
    bindings.map { case (key, value) => (key, curate(value)) }
}
