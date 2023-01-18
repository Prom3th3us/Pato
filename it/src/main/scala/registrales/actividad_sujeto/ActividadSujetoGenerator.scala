package registrales.actividad_sujeto

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoCommands.ActividadSujetoUpdateFromDto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto.ActividadSujeto
import consumers.registral.actividad_sujeto.domain.ActividadSujetoEvents.ActividadSujetoUpdatedFromDto
import consumers.registral.actividad_sujeto.infrastructure.json._
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto.ActividadSujeto
import generator.Generator.{deliveryId, loadExample}
import generator.{Generator, Helper}

class ActividadSujetoGenerator extends Generator[ActividadSujeto] {
  import ActividadSujetoGenerator._
  lazy val example = loadExample[ActividadSujeto]("assets/examples/DGR-COP-ACTIVIDADES.json")

  def next(id: Int): ActividadSujeto =
    example.copy(
      EV_ID = id.toString,
      BAT_SUJ_IDENTIFICADOR = id.toString,
      BAT_ATD_ID = id.toString
    )

  override def toJson(e: ActividadSujeto): String = e.toJson

  override def aggregateRoot(e: ActividadSujeto): String = e.aggregateRoot
}

object ActividadSujetoGenerator {
  implicit class ActividadSujetoGeneratorHelper(ActividadSujeto: ActividadSujeto) extends Helper {

    def toJson: String =
      serialization.encode(ActividadSujeto)

    def toEvent: ActividadSujetoUpdatedFromDto =
      ActividadSujetoUpdatedFromDto(deliveryId,
                                    ActividadSujeto.BAT_SUJ_IDENTIFICADOR,
                                    ActividadSujeto.BAT_ATD_ID,
                                    ActividadSujeto)

    def toCommand: ActividadSujetoUpdateFromDto =
      ActividadSujetoUpdateFromDto(ActividadSujeto.BAT_SUJ_IDENTIFICADOR,
                                   ActividadSujeto.BAT_ATD_ID,
                                   deliveryId,
                                   ActividadSujeto)

  }
}
