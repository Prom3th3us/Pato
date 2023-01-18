package utils.implicits

import scala.collection.immutable

object MapT {
  implicit class PrettyPrintMap[K, V](val map: Map[K, V]) {

    def removeEmpty(expectation: String): String =
      (expectation match {
        case s"$value -> $expectation"
            if expectation
              .replace(Console.CYAN, "")
              .replace(Console.RESET, "")
              .trim
              .isEmpty =>
          None
        case _ => Some(expectation)
      }).getOrElse(indentLine("."))

    def prettyPrintAlongside(other: Map[K, V]): String = {
      val myPrettyPrint = map
        .filter {
          case (k, v) => other.keys.toSet contains k
        }
        .prettyPrint
        .toString
        .split("\n")
        .sorted
      val otherPrettyPrint = other.prettyPrint.toString.split("\n").sorted //.padTo(myPrettyPrint.length, "")

      val desiredTabulation = 70

      val header = " " * 10 + headerColor("at CASSANDRA") + "." * 65 + ". " + headerColor("the EXPECTED value is:") + "\n"

      header +
      (myPrettyPrint zip otherPrettyPrint)
        .map {
          case (a: String, b) =>
            val aLimited = a.take(desiredTabulation)
            val aSize = aLimited
              .replace(Console.CYAN, "")
              .replace(Console.RESET, "")
              .trim
              .length
            val delta = desiredTabulation - aSize
            aLimited + "." * delta + "  | " + removeEmpty(b.take(desiredTabulation))
        }
        .mkString("\n")
    }

    def prettyPrint: PrettyPrintMap[K, V] = this

    override def toString: String = toStringLines.mkString("\n")

    def indentLine(line: String): String = " " * 10 + line

    def toStringLines: immutable.Iterable[String] = {
      map
        .flatMap { case (k, v) => keyValueToString(k, v) } map indentLine
    }

    def headerColor(header: String) = s"${Console.CYAN_B} $header ${Console.RESET}"

    def keyColor(k: K) = s"${Console.YELLOW} $k ${Console.RESET}"

    def valueColor(v: V) =
      s"${Console.CYAN} ${v match {
        case "null" => "";
        case other => other
      }} ${Console.RESET}"

    def keyValueToString(k: K, value: V): Iterable[String] = {
      value match {
        case v: Map[_, _] => Iterable(s"${keyColor(k)} -> Map (") ++ v.prettyPrint.toStringLines ++ Iterable(")}")
        case x => Iterable(s"${keyColor(k)} -> ${valueColor(x)}")
      }
    }

  }
}
