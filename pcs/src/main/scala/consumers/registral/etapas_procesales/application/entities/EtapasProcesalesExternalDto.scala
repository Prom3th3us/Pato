package consumers.registral.etapas_procesales.application.entities

import java.time.LocalDateTime

import play.api.libs.json.JsObject

sealed trait EtapasProcesalesExternalDto extends ddd.ExternalDto {

  def EV_ID: String

  def BEP_JUI_ID: String

  def BPE_ETA_ID: String

  def BEP_DESCRIPCION: Option[String]

  def BEP_FECHA_FIN: Option[LocalDateTime]

  def BEP_FECHA_INICIO: Option[LocalDateTime]

  def BEP_OTROS_ATRIBUTOS: JsObject

  def BEP_REFERENCIA: Option[String]

  def BEP_TIPO: Option[String]

}

object EtapasProcesalesExternalDto {

  case class EtapasProcesalesTri(EV_ID: String,
                                 BEP_JUI_ID: String,
                                 BPE_ETA_ID: String,
                                 BEP_DESCRIPCION: Option[String],
                                 BEP_FECHA_FIN: Option[LocalDateTime],
                                 BEP_FECHA_INICIO: Option[LocalDateTime],
                                 BEP_OTROS_ATRIBUTOS: JsObject,
                                 BEP_REFERENCIA: Option[String],
                                 BEP_TIPO: Option[String])
      extends EtapasProcesalesExternalDto

  case class EtapasProcesalesAnt(EV_ID: String,
                                 BEP_JUI_ID: String,
                                 BPE_ETA_ID: String,
                                 BEP_DESCRIPCION: Option[String],
                                 BEP_FECHA_FIN: Option[LocalDateTime],
                                 BEP_FECHA_INICIO: Option[LocalDateTime],
                                 BEP_OTROS_ATRIBUTOS: JsObject,
                                 BEP_REFERENCIA: Option[String],
                                 BEP_TIPO: Option[String])
      extends EtapasProcesalesExternalDto

}
