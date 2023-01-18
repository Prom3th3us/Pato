package generator

import java.util.concurrent.atomic.AtomicInteger

import ddd.ExternalDto
import generator.Generator.KafkaKeyValue
import play.api.libs.json.Format
import serialization.decode

import scala.io.Source
import scala.reflect.ClassTag

trait Generator[E <: ExternalDto] {
  def example: E
  def next(id: Int): E
  def toJson(e: E): String
  def aggregateRoot(e: E): String

  final def nextKafkaKeyValue(i: Int): KafkaKeyValue = {
    val e = next(i)
    KafkaKeyValue(
      aggregateRoot = aggregateRoot(e),
      json = toJson(e)
    )
  }

}
object Generator {
  case class KafkaKeyValue(aggregateRoot: String, json: String) {
    def key = aggregateRoot
    def value = json
  }

  def loadExample[A <: ddd.ExternalDto: ClassTag](path: String)(implicit format: Format[A]): A = {
    val source = Source.fromFile(path)
    val text = source.getLines mkString "\n"
    source.close()
    decode[A](text) match {
      case Left(explanation) =>
        // log.error(explanation)
        throw new Exception("Failed to load example")
      case Right(value) => value
    }
  }

  val nextInt = new AtomicInteger()
  val deliveryId = nextInt.incrementAndGet()
}
