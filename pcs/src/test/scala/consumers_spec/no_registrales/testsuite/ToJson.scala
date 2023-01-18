package consumers_spec.no_registrales.testsuite

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{ObjetosAnt, ObjetosTri}
import consumers.no_registral.objeto.infrastructure.json._
import consumers.no_registral.obligacion.application.entities.ObligacionCommands.{
  ObligacionUpdateExencion,
  ObligacionUpdateFromDto
}
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{ObligacionesAnt, ObligacionesTri}
import consumers.no_registral.obligacion.infrastructure.json._
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.{SujetoAnt, SujetoTri}
import consumers.no_registral.sujeto.infrastructure.json._

object ToJson {
  implicit class ObligacionesAntToJson(dto: ObligacionesAnt) {
    def toJson: String =
      serialization
        .encode[ObligacionesAnt](dto)
  }
  implicit class ObligacionesTriToJson(dto: ObligacionesTri) {
    def toJson: String =
      serialization
        .encode[ObligacionesTri](dto)
  }
  implicit class ObligacionExternalDtoToJson(dto: ObligacionExternalDto) {
    def toJson: String = dto match {
      case dto: ObligacionesAnt => dto.toJson
      case dto: ObligacionesTri => dto.toJson
    }
  }
  implicit class ObjetosAntToJson(dto: ObjetosAnt) {
    def toJson: String =
      serialization.encode[ObjetosAnt](dto)
  }
  implicit class ObjetosTriToJson(dto: ObjetosTri) {
    def toJson: String =
      serialization.encode[ObjetosTri](dto)
  }
  implicit class ObjetoExternalDtoToJson(dto: ObjetoExternalDto) {
    def toJson: String = dto match {
      case dto: ObjetosAnt => dto.toJson
      case dto: ObjetosTri => dto.toJson
    }
  }
  implicit class SujetosAntToJson(dto: SujetoAnt) {
    def toJson: String =
      serialization.encode[SujetoAnt](dto)
  }
  implicit class SujetosTriToJson(dto: SujetoTri) {
    def toJson: String =
      serialization.encode[SujetoTri](dto)
  }
  implicit class SujetoExternalDtoToJson(dto: SujetoExternalDto) {
    def toJson: String = dto match {
      case dto: SujetoAnt => dto.toJson
      case dto: SujetoTri => dto.toJson
    }
  }

  implicit class ObligacionUpdateFromDtoToJson(dto: ObligacionUpdateFromDto) {
    def toJson: String =
      serialization.encode[ObligacionUpdateFromDto](dto)
  }
  implicit class ObligacionUpdateExencionToJson(dto: ObligacionUpdateExencion) {
    def toJson: String =
      serialization.encode[ObligacionUpdateExencion](dto)
  }
}
