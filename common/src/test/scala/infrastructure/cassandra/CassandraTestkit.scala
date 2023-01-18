package infrastructure.cassandra

import java.time.{LocalDate, LocalDateTime}

import scala.concurrent.duration._
import scala.util.Try

import org.scalatest.Assertion
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import play.api.libs.json.JsObject
import utils.implicits.MapT._
import utils.implicits.RowT._

object CassandraTestkit extends AnyFlatSpec {

  def standarize(s: String): String = {
    val firstPass =
      s.replace("'", "")
        .replace("Map()", "{}")
        .replace("--", "null")
        .replace("\"", "")
        .replace(".", "")

    val secondPass = firstPass match {
      case s"Some($v)" => v
      case s"None" => "null"
      case s"($v)" => v
      case s"$year-$month-${day}T$hour:$minute:$second.$millis" => s"$year-$month-$day"
      case other => other
    }

    val thirdPass = secondPass.toLowerCase
      .take(20)

    Try {
      thirdPass.toFloat.toString
    }.toEither match {
      case Left(_) => thirdPass
      case Right(n) => n
    }
  }

  def safeCompare(a: String, b: String): Boolean = {
    standarize(a) == standarize(b)
  }

  def castToString(a: Any): String = a match {
    case v: Boolean => v.toString
    case Some(v: Boolean) => v.toString

    case v: Int => v.toString

    case Some(v: Int) => v.toString

    case v: BigInt => v.toString

    case Some(v: BigInt) => v.toString

    case v: BigDecimal => v.toString

    case Some(v: BigDecimal) => v.toString

    case v: JsObject => v.toString

    case Some(v: JsObject) => v.toString

    case v: Map[_, _] if v.isEmpty => "{}"

    case v: Map[_, _] if v.nonEmpty && v.values.head.isInstanceOf[String] && v.keys.head.isInstanceOf[String] =>
      v.toString

    case Some(v: Map[_, _]) if v.nonEmpty && v.values.head.isInstanceOf[String] && v.keys.head.isInstanceOf[String] =>
      v.toString

    case v: String => v.toString

    case Some(v: String) => v.toString

    case v: LocalDateTime => v.toString

    case Some(v: LocalDateTime) => v.toString

    case v: LocalDate => v.toString

    case Some(v: LocalDate) => v.toString

    case _ => "null"
  }

  case class TableName(tableName: String)

  implicit class RowValidation(rowAsMap: Map[String, String]) {

    def prepareForComparison(unpreparedMap: Map[String, Any]) =
      unpreparedMap.map {
        case (k, v) => (k, castToString(v))
      }

    def =========================(compareTo: Map[String, Any]): Assertion = {
      val rowAsMapPrepared = this prepareForComparison rowAsMap
      val compareToPrepared = this prepareForComparison compareTo

      if (compareToPrepared.isEmpty) {
        assert(rowAsMapPrepared.isEmpty)
      } else {
        val decoration = Console.YELLOW + "-" * 20 + Console.RESET
        //val header = " " * 40 + decoration + " " + tableName + " " + decoration
        val twoColumnComparison = rowAsMapPrepared prettyPrintAlongside compareToPrepared
        val detailedOneByOneComparison: Seq[String] = compareToPrepared.toSeq map {
            case (key, value) =>
              val valueAsString = value
              val valueInCassandra = rowAsMapPrepared.getOrElse(key, "--")
              " " * 60 + s"row(${Console.CYAN + key + Console.RESET}) == value" + " | " + s"${Console.GREEN + standarize(
                valueInCassandra
              ) + Console.RESET} == ${standarize(valueAsString)}"
          }
        val fullPresentation: String = (Seq(twoColumnComparison) ++ detailedOneByOneComparison) mkString "\n"
        val twoEmptyLines = "\n" * 2
        println(twoEmptyLines + fullPresentation + twoEmptyLines)
      }

      val comparisons = compareToPrepared map {
          case (key, value) =>
            val valueAsString = value
            val valueInCassandra = rowAsMapPrepared.getOrElse(key, "--")
            assert(safeCompare(valueInCassandra, valueAsString))
            safeCompare(valueInCassandra, valueAsString)

        }
      if (comparisons.isEmpty) assert(true)
      else assert(comparisons reduce (_ && _))
    }

  }

  implicit class CassandraTestkit(db: CassandraClient) extends AnyFlatSpec with ScalaFutures {

    def validate(query: String)(compareTo: Map[String, String])(implicit tableName: TableName): Assertion = {
      Thread.sleep(5000)
      println(Console.CYAN_B + "QUERY " + Console.RESET + Console.BLUE + query + Console.RESET)
      whenReady(db.cqlQuerySingleResult(query), timeout(60 seconds), interval(1 second)) {
        case None => assert(false)
        case Some(row) =>
          val rowAsMap = row.toMap
          rowAsMap ========================= compareTo
      }
    }
  }
}
