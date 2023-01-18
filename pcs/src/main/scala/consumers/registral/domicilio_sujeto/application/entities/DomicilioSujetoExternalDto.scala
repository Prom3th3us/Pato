package consumers.registral.domicilio_sujeto.application.entities

sealed trait DomicilioSujetoExternalDto extends ddd.ExternalDto {
  def EV_ID: String
  def BDS_SUJ_IDENTIFICADOR: String
  def BDS_DOM_ID: String
  def BDS_BARRIO: Option[String]
  def BDS_CALLE: Option[String]
  def BDS_CODIGO_POSTAL: Option[String]
  def BDS_DPTO: Option[String]
  def BDS_ESTADO: Option[String]
  def BDS_KILOMETRO: Option[String]
  def BDS_LOCALIDAD: Option[String]
  def BDS_LOTE: Option[String]
  def BDS_MANZANA: Option[String]
  def BDS_PISO: Option[String]
  def BDS_PROVINCIA: Option[String]
  def BDS_PUERTA: Option[String]
  def BDS_TIPO: Option[String]
  def BDS_TORRE: Option[String]
  def BDS_OBSERVACIONES: Option[String]
}
object DomicilioSujetoExternalDto {
  case class DomicilioSujetoTri(EV_ID: String,
                                BDS_SUJ_IDENTIFICADOR: String,
                                BDS_DOM_ID: String,
                                BDS_BARRIO: Option[String],
                                BDS_CALLE: Option[String],
                                BDS_CODIGO_POSTAL: Option[String],
                                BDS_DPTO: Option[String],
                                BDS_ESTADO: Option[String],
                                BDS_KILOMETRO: Option[String],
                                BDS_LOCALIDAD: Option[String],
                                BDS_LOTE: Option[String],
                                BDS_MANZANA: Option[String],
                                BDS_PISO: Option[String],
                                BDS_PROVINCIA: Option[String],
                                BDS_PUERTA: Option[String],
                                BDS_TIPO: Option[String],
                                BDS_TORRE: Option[String],
                                BDS_OBSERVACIONES: Option[String])
      extends DomicilioSujetoExternalDto

  case class DomicilioSujetoAnt(EV_ID: String,
                                BDS_SUJ_IDENTIFICADOR: String,
                                BDS_DOM_ID: String,
                                BDS_BARRIO: Option[String],
                                BDS_CALLE: Option[String],
                                BDS_CODIGO_POSTAL: Option[String],
                                BDS_DPTO: Option[String],
                                BDS_ESTADO: Option[String],
                                BDS_KILOMETRO: Option[String],
                                BDS_LOCALIDAD: Option[String],
                                BDS_LOTE: Option[String],
                                BDS_MANZANA: Option[String],
                                BDS_PISO: Option[String],
                                BDS_PROVINCIA: Option[String],
                                BDS_PUERTA: Option[String],
                                BDS_TIPO: Option[String],
                                BDS_TORRE: Option[String],
                                BDS_OBSERVACIONES: Option[String])
      extends DomicilioSujetoExternalDto

}
