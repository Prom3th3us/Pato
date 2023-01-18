package consumers.registral.juicio.application.entities

import java.time.LocalDateTime

import play.api.libs.json.JsObject

sealed trait JuicioExternalDto extends ddd.ExternalDto {
  def EV_ID: String
  def BJU_SUJ_IDENTIFICADOR: String
  def BJU_SOJ_TIPO_OBJETO: String
  def BJU_SOJ_IDENTIFICADOR: String
  def BJU_JUI_ID: String
  def BJU_NRO_EXTERNO:Option[String]
  def BJU_CAPITAL: Option[BigDecimal]
  def BJU_ESTADO: Option[String]
  def BJU_FISCALIZADA: Option[String]
  def BJU_GASTOS: Option[BigDecimal]
  def BJU_GASTOS_MART: Option[BigDecimal]
  def BJU_HONORARIOS: Option[BigDecimal]
  def BJU_HONORARIOS_MART: Option[BigDecimal]
  def BJU_INICIO_DEMANDA: Option[LocalDateTime]
  def BJU_INTERES_PUNIT: Option[BigDecimal]
  def BJU_INTERES_RESAR: Option[BigDecimal]
  def BJU_PCR_ID: Option[BigInt]
  def BJU_PORCENTAJE_IVA: Option[BigDecimal]
  def BJU_PROCURADOR: Option[String]
  def BJU_TIPO: Option[String]
  def BJU_TOTAL: Option[BigDecimal]
  def BJU_OTROS_ATRIBUTOS: JsObject
}

object JuicioExternalDto {

  case class JuicioAnt(
      EV_ID: String,
      BJU_SUJ_IDENTIFICADOR: String,
      BJU_SOJ_TIPO_OBJETO: String,
      BJU_SOJ_IDENTIFICADOR: String,
      BJU_JUI_ID: String,
      BJU_NRO_EXTERNO: Option[String],
      BJU_CAPITAL: Option[BigDecimal],
      BJU_ESTADO: Option[String],
      BJU_FISCALIZADA: Option[String],
      BJU_GASTOS: Option[BigDecimal],
      BJU_GASTOS_MART: Option[BigDecimal],
      BJU_HONORARIOS: Option[BigDecimal],
      BJU_HONORARIOS_MART: Option[BigDecimal],
      BJU_INICIO_DEMANDA: Option[LocalDateTime],
      BJU_INTERES_PUNIT: Option[BigDecimal],
      BJU_INTERES_RESAR: Option[BigDecimal],
      BJU_PCR_ID: Option[BigInt],
      BJU_PORCENTAJE_IVA: Option[BigDecimal],
      BJU_PROCURADOR: Option[String],
      BJU_TIPO: Option[String],
      BJU_TOTAL: Option[BigDecimal],
      BJU_OTROS_ATRIBUTOS: JsObject
  ) extends JuicioExternalDto

  case class JuicioTri(
      EV_ID: String,
      BJU_SUJ_IDENTIFICADOR: String,
      BJU_SOJ_TIPO_OBJETO: String,
      BJU_SOJ_IDENTIFICADOR: String,
      BJU_JUI_ID: String,
      BJU_NRO_EXTERNO: Option[String],
      BJU_CAPITAL: Option[BigDecimal],
      BJU_ESTADO: Option[String],
      BJU_FISCALIZADA: Option[String],
      BJU_GASTOS: Option[BigDecimal],
      BJU_GASTOS_MART: Option[BigDecimal],
      BJU_HONORARIOS: Option[BigDecimal],
      BJU_HONORARIOS_MART: Option[BigDecimal],
      BJU_INICIO_DEMANDA: Option[LocalDateTime],
      BJU_INTERES_PUNIT: Option[BigDecimal],
      BJU_INTERES_RESAR: Option[BigDecimal],
      BJU_PCR_ID: Option[BigInt],
      BJU_PORCENTAJE_IVA: Option[BigDecimal],
      BJU_PROCURADOR: Option[String],
      BJU_TIPO: Option[String],
      BJU_TOTAL: Option[BigDecimal],
      BJU_OTROS_ATRIBUTOS: JsObject
  ) extends JuicioExternalDto

  case class DetallesJuicio(
      BJU_OBLIGACION: String, // "19940000000007261586",
      BJU_IMPUESTO: String, // "5",
      BJU_CONCEPTO: String, // "201",
      BJU_PERIODO: String, // "1994",
      BJU_CUOTA: String, // "10",
      BJU_FECHA_GENERACION: Option[LocalDateTime], // "1999-12-29 00:00:00.0",
      BJU_FECHA_IMPRESION: Option[String], // null,
      BJU_VTO_ORIGINAL: Option[LocalDateTime], // "1994-03-23 00:00:00.0",
      BJU_IMPORTE_ORIGINAL: Option[String], // "34",
      BJU_IMPORTE_HISTORICO: Option[String], // "34",
      BJU_INTERES: Option[BigDecimal], // "54.22",
      BJU_INTERES_LIQUIDACION: Option[String], // null,
      BJU_ID_MARTILLERO_OTROS_ATRIBUTOS: Option[String], // null,
      BJU_MARTILLERO_OTROS_ATRIBUTOS: Option[String] // null
  )

}
