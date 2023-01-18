package consumers.registral.subasta.application.entities

import java.time.LocalDateTime

case class SubastaExternalDto(BSB_SUB_ID: String,
                              EV_ID: String,
                              BSB_SUJ_IDENTIFICADOR_ADQ: String,
                              BSB_SOJ_TIPO_OBJETO: String,
                              BSB_SOJ_IDENTIFICADOR: String,
                              BSB_AUTO: Option[String],
                              BSB_FECHA_FIN: Option[LocalDateTime],
                              BSB_FECHA_INICIO: Option[LocalDateTime],
                              BSB_SUJ_IDENTIFICADOR_SUB: Option[String],
                              BSB_TIPO: Option[String])
    extends ddd.ExternalDto
