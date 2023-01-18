package consumers.registral.calendario.application.entities

import java.time.LocalDateTime

import ddd.ExternalDto

case class CalendarioExternalDto(
    EV_ID: String,
    BCL_IDENTIFICADOR: String,
    BCL_FECHA: LocalDateTime,
    BCL_DESCRIPCION: Option[String],
    BCL_TIPO: Option[String]
) extends ExternalDto
