package consumers.registral.parametrica_recargo.application.entities

import java.time.LocalDateTime

sealed trait ParametricaRecargoExternalDto extends ddd.ExternalDto {

  def EV_ID: String

  def BPR_INDICE: String

  def BPR_TIPO_INDICE: String

  def BPR_DESCRIPCION: Option[String]

  def BPR_FECHA_DESDE: LocalDateTime

  def BPR_FECHA_HASTA: Option[LocalDateTime]

  def BPR_VALOR: Option[BigDecimal]

  def BPR_IMPUESTO: String

  def BPR_CONCEPTO: String

  def BPR_PERIODO: String

}

object ParametricaRecargoExternalDto {

  case class ParametricaRecargoTri(EV_ID: String,
                                   BPR_INDICE: String,
                                   BPR_TIPO_INDICE: String,
                                   BPR_DESCRIPCION: Option[String],
                                   BPR_FECHA_DESDE: LocalDateTime,
                                   BPR_FECHA_HASTA: Option[LocalDateTime],
                                   BPR_VALOR: Option[BigDecimal],
                                   BPR_IMPUESTO: String,
                                   BPR_CONCEPTO: String,
                                   BPR_PERIODO: String)
      extends ParametricaRecargoExternalDto

  case class ParametricaRecargoAnt(EV_ID: String,
                                   BPR_INDICE: String,
                                   BPR_TIPO_INDICE: String,
                                   BPR_DESCRIPCION: Option[String],
                                   BPR_FECHA_DESDE: LocalDateTime,
                                   BPR_FECHA_HASTA: Option[LocalDateTime],
                                   BPR_VALOR: Option[BigDecimal],
                                   BPR_IMPUESTO: String,
                                   BPR_CONCEPTO: String,
                                   BPR_PERIODO: String)
      extends ParametricaRecargoExternalDto

}
