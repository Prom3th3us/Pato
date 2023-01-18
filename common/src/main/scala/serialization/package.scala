import oracle.Oracle.connOracleKafkaToWriteside
import play.api.libs.json.{Format, JsValue, Json}

import scala.reflect.ClassTag

package object serialization {

  def toJsValue[A](a: A)(implicit format: Format[A]): JsValue =
    format.writes(a)

  def encode[A](a: A)(implicit format: Format[A]): String =
    Json.prettyPrint(format.writes(a))

  // This function will throw an exception to be catched by the surrounding Transaction
  def decodeF[DTO: ClassTag](input: String)(implicit format: Format[DTO]): DTO = {
    serialization.decode[DTO](input) match {
      case Left(error) => throw new SerializationError(error) // to be catched by transactional
      case Right(value) => value
    }
  }

  def decode[A: ClassTag](a: String)(implicit format: Format[A]): Either[String, A] = {
    def ctag = implicitly[reflect.ClassTag[A]]
    def AClass: Class[A] = ctag.runtimeClass.asInstanceOf[Class[A]]
    if (AClass.getName == "java.lang.String") {
      Right(a.asInstanceOf[A])
    } else {
      Json.parse(a).asOpt[A] match {
        case Some(a) => Right(a)
        case None => Left(s"Failed to decode ${AClass.getName} $a)}")
      }
    }
  }

  def maybeDecode[A: ClassTag](a: String)(implicit format: Format[A]): Either[Throwable, A] = {
    def ctag = implicitly[reflect.ClassTag[A]]
    def AClass: Class[A] = ctag.runtimeClass.asInstanceOf[Class[A]]
    if (AClass.getName == "java.lang.String") {
      Right(a.asInstanceOf[A])
    } else {
      Json.parse(a).asOpt[A] match {
        case Some(a) => Right(a)
        case None =>
          Left(
            new SerializationError(
              s"""
              Failed to decode ${AClass.getName}
              because of:
              ${Json.parse(a).validate}
              message that failed was: 
              $a
              """
            )
          )
      }
    }
  }

  class SerializationError(message: String) extends Exception {
    override def getMessage: String = message
  }
}
