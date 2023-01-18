package consumers.registral.domicilio_objeto.application.entities

sealed trait DomicilioObjetoExternalDto extends ddd.ExternalDto {
  def EV_ID: String
  def BDO_SUJ_IDENTIFICADOR: String
  def BDO_SOJ_TIPO_OBJETO: String
  def BDO_SOJ_IDENTIFICADOR: String
  def BDO_DOM_ID: String
  def BDO_BARRIO: Option[String]
  def BDO_CALLE: Option[String]
  def BDO_CODIGO_POSTAL: Option[String]
  def BDO_DPTO: Option[String]
  def BDO_ESTADO: Option[String]
  def BDO_KILOMETRO: Option[String]
  def BDO_LOCALIDAD: Option[String]
  def BDO_LOTE: Option[String]
  def BDO_MANZANA: Option[String]
  def BDO_PISO: Option[String]
  def BDO_PROVINCIA: Option[String]
  def BDO_PUERTA: Option[String]
  def BDO_TIPO: Option[String]
  def BDO_TORRE: Option[String]
  def BDO_OBSERVACIONES: Option[String]
}
object DomicilioObjetoExternalDto {
  case class DomicilioObjetoTri(EV_ID: String,
                                BDO_SUJ_IDENTIFICADOR: String,
                                BDO_SOJ_TIPO_OBJETO: String,
                                BDO_SOJ_IDENTIFICADOR: String,
                                BDO_DOM_ID: String,
                                BDO_BARRIO: Option[String],
                                BDO_CALLE: Option[String],
                                BDO_CODIGO_POSTAL: Option[String],
                                BDO_DPTO: Option[String],
                                BDO_ESTADO: Option[String],
                                BDO_KILOMETRO: Option[String],
                                BDO_LOCALIDAD: Option[String],
                                BDO_LOTE: Option[String],
                                BDO_MANZANA: Option[String],
                                BDO_PISO: Option[String],
                                BDO_PROVINCIA: Option[String],
                                BDO_PUERTA: Option[String],
                                BDO_TIPO: Option[String],
                                BDO_TORRE: Option[String],
                                BDO_OBSERVACIONES: Option[String])
      extends DomicilioObjetoExternalDto

  case class DomicilioObjetoAnt(EV_ID: String,
                                BDO_SUJ_IDENTIFICADOR: String,
                                BDO_SOJ_TIPO_OBJETO: String,
                                BDO_SOJ_IDENTIFICADOR: String,
                                BDO_DOM_ID: String,
                                BDO_BARRIO: Option[String],
                                BDO_CALLE: Option[String],
                                BDO_CODIGO_POSTAL: Option[String],
                                BDO_DPTO: Option[String],
                                BDO_ESTADO: Option[String],
                                BDO_KILOMETRO: Option[String],
                                BDO_LOCALIDAD: Option[String],
                                BDO_LOTE: Option[String],
                                BDO_MANZANA: Option[String],
                                BDO_PISO: Option[String],
                                BDO_PROVINCIA: Option[String],
                                BDO_PUERTA: Option[String],
                                BDO_TIPO: Option[String],
                                BDO_TORRE: Option[String],
                                BDO_OBSERVACIONES: Option[String])
      extends DomicilioObjetoExternalDto

}
