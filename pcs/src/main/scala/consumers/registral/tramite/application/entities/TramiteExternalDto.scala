package consumers.registral.tramite.application.entities

import java.time.LocalDateTime

object TramiteExternalDto {
  case class Tramite(EV_ID: String,
                     BTR_SUJ_IDENTIFICADOR: String,
                     BTR_TRMID: String,
                     BTR_ARCHIVOS: Map[String, String],
                     BTR_DESCRIPCION: Option[String],
                     BTR_ESTADO: Option[String],
                     BTR_FECHA_INICIO: Option[LocalDateTime],
                     BTR_OTROS_ATRIBUTOS: Map[String, String],
                     BTR_REFERENCIA: Option[String],
                     BTR_TIPO: Option[String])
      extends ddd.ExternalDto
}
