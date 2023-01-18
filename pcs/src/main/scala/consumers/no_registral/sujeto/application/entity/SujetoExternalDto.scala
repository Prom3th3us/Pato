package consumers.no_registral.sujeto.application.entity

import ddd.ExternalDto

sealed trait SujetoExternalDto extends ExternalDto {
  def SUJ_IDENTIFICADOR: String
  def SUJ_CAT_SUJ_ID: Option[BigInt]
  def SUJ_DENOMINACION: Option[String]
  def SUJ_DFE: Option[String]
  def SUJ_DIRECCION: Option[String]
  def SUJ_EMAIL: Option[String]
  def SUJ_ID_EXTERNO: Option[String]
  def SUJ_OTROS_ATRIBUTOS: Option[Map[String, String]]
  def SUJ_RIESGO_FISCAL: Option[String]
  def SUJ_SITUACION_FISCAL: Option[String]
  def SUJ_TELEFONO: Option[String]
  def SUJ_TIPO: Option[String]
  def SUJ_CANAL_ORIGEN: Option[String]
  def EV_ID: BigInt
}

object SujetoExternalDto {
  case class SujetoAnt(
      EV_ID: BigInt,
      SUJ_IDENTIFICADOR: String,
      SUJ_CAT_SUJ_ID: Option[BigInt],
      SUJ_DENOMINACION: Option[String],
      SUJ_DFE: Option[String],
      SUJ_DIRECCION: Option[String],
      SUJ_EMAIL: Option[String],
      SUJ_ID_EXTERNO: Option[String],
      SUJ_OTROS_ATRIBUTOS: Option[Map[String, String]],
      SUJ_RIESGO_FISCAL: Option[String],
      SUJ_SITUACION_FISCAL: Option[String],
      SUJ_TELEFONO: Option[String],
      SUJ_TIPO: Option[String],
      SUJ_CANAL_ORIGEN: Option[String]
  ) extends SujetoExternalDto

  case class SujetoTri(
      EV_ID: BigInt,
      SUJ_IDENTIFICADOR: String,
      SUJ_CAT_SUJ_ID: Option[BigInt],
      SUJ_DENOMINACION: Option[String],
      SUJ_DFE: Option[String],
      SUJ_DIRECCION: Option[String],
      SUJ_EMAIL: Option[String],
      SUJ_ID_EXTERNO: Option[String],
      SUJ_OTROS_ATRIBUTOS: Option[Map[String, String]],
      SUJ_RIESGO_FISCAL: Option[String],
      SUJ_SITUACION_FISCAL: Option[String],
      SUJ_TELEFONO: Option[String],
      SUJ_TIPO: Option[String],
      SUJ_CANAL_ORIGEN: Option[String]
  ) extends SujetoExternalDto

}
