package consumers.registral.declaracion_jurada.application.entities

import java.time.LocalDateTime

import play.api.libs.json.JsObject
sealed trait DeclaracionJuradaExternalDto extends ddd.ExternalDto {
  def EV_ID: String
  def BDJ_DDJ_ID: String
  def BDJ_SUJ_IDENTIFICADOR: String
  def BDJ_SOJ_TIPO_OBJETO: String
  def BDJ_SOJ_IDENTIFICADOR: String
  def BDJ_CUOTA: Option[String]
  def BDJ_ESTADO: Option[String]
  def BDJ_FISCALIZADA: Option[String]
  def BDJ_IMPUESTO_DETERMINADO: Option[BigDecimal]
  def BDJ_OBN_ID: Option[String]
  def BDJ_OTROS_ATRIBUTOS: JsObject
  def BDJ_PERCEPCIONES: Option[BigDecimal]
  def BDJ_PERIODO: Option[String]
  def BDJ_PRORROGA: Option[LocalDateTime]
  def BDJ_RECAUDACIONES: Option[BigDecimal]
  def BDJ_RETENCIONES: Option[BigDecimal]
  def BDJ_TIPO: Option[String]
  def BDJ_TOTAL: Option[BigDecimal]
  def BDJ_VENCIMIENTO: Option[LocalDateTime]
}
object DeclaracionJuradaExternalDto {
  case class DeclaracionJurada(EV_ID: String,
                               BDJ_DDJ_ID: String,
                               BDJ_SUJ_IDENTIFICADOR: String,
                               BDJ_SOJ_TIPO_OBJETO: String,
                               BDJ_SOJ_IDENTIFICADOR: String,
                               BDJ_CUOTA: Option[String],
                               BDJ_ESTADO: Option[String],
                               BDJ_FISCALIZADA: Option[String],
                               BDJ_IMPUESTO_DETERMINADO: Option[BigDecimal],
                               BDJ_OBN_ID: Option[String],
                               BDJ_OTROS_ATRIBUTOS: JsObject,
                               BDJ_PERCEPCIONES: Option[BigDecimal],
                               BDJ_PERIODO: Option[String],
                               BDJ_PRORROGA: Option[LocalDateTime],
                               BDJ_RECAUDACIONES: Option[BigDecimal],
                               BDJ_RETENCIONES: Option[BigDecimal],
                               BDJ_TIPO: Option[String],
                               BDJ_TOTAL: Option[BigDecimal],
                               BDJ_VENCIMIENTO: Option[LocalDateTime])
      extends DeclaracionJuradaExternalDto
}
