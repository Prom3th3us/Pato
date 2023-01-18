package akka.entity

import scala.io.Source
import scala.io.Codec
import java.nio.charset.CodingErrorAction

object EntityIdOps {
  val mapping: Map[Char, Int] = {
    val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)
    Source
      .fromResource("UTF-8-character-table")(decoder)
      .getLines
      .toSeq
      .map(_ trim)
      .map(_(0))
      .zipWithIndex
      .toMap

  }
  def encode(aggregateRootCandidate: String): String =
    aggregateRootCandidate
      .map { letter =>
        mapping(letter)
      }
      .map(_ toString)
      .reduce(_ + "|" + _)

  def decode(encoded: String): String =
    encoded
      .split('|')
      .map(_ toInt)
      .map(mapping.map(_ swap)(_))
      .map(_ toString)
      .reduce(_ + _)

  def fromEncodedToBigInt(encoded: String): BigInt = BigInt(encoded.split('|').mkString(""))
}
