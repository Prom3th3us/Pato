package consumers.registral.actividad_sujeto.application.entities

import java.time.LocalDateTime

import ddd.ExternalDto
import play.api.libs.json.JsObject

sealed trait ActividadSujetoExternalDto extends ExternalDto {
  def EV_ID: String
  def BAT_SUJ_IDENTIFICADOR: String
  def BAT_ATD_ID: String
  def BAT_DESCRIPCION: Option[String]
  def BAT_FECHA_INICIO: Option[LocalDateTime]
  def BAT_FECHA_FIN: Option[LocalDateTime]
  def BAT_OTROS_ATRIBUTOS: JsObject
  def BAT_REFERENCIA: Option[String]
  def BAT_TIPO: Option[String]
}

object ActividadSujetoExternalDto {
  case class ActividadSujeto(
      EV_ID: String,
      BAT_SUJ_IDENTIFICADOR: String,
      BAT_ATD_ID: String,
      BAT_DESCRIPCION: Option[String],
      BAT_FECHA_INICIO: Option[LocalDateTime],
      BAT_FECHA_FIN: Option[LocalDateTime],
      BAT_OTROS_ATRIBUTOS: JsObject,
      BAT_REFERENCIA: Option[String],
      BAT_TIPO: Option[String]
  ) extends ActividadSujetoExternalDto
}
