package no_registrales.sujeto

import consumers.no_registral.sujeto.application.entity.SujetoCommands.SujetoUpdateFromAnt
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.SujetoAnt
import consumers.no_registral.sujeto.domain.SujetoEvents.SujetoUpdatedFromAnt
import consumers.no_registral.sujeto.infrastructure.json._
import generator.Generator.{deliveryId, loadExample}
import generator.{Generator, Helper}

class SujetoAntGenerator extends Generator[SujetoAnt] {
  import SujetoAntGenerator._
  lazy val example = loadExample[SujetoAnt]("assets/examples/DGR-COP-SUJETO-ANT.json")

  def next(id: Int): SujetoAnt =
    example.copy(
      EV_ID = id,
      SUJ_IDENTIFICADOR = id.toString
    )

  override def toJson(e: SujetoAnt): String = e.toJson

  override def aggregateRoot(e: SujetoAnt): String = e.aggregateRoot
}

object SujetoAntGenerator {
  implicit class SujetoAntGeneratorHelper(sujetoAnt: SujetoAnt) extends Helper {

    def toJson: String =
      serialization.encode(sujetoAnt)

    def toEvent: SujetoUpdatedFromAnt =
      SujetoUpdatedFromAnt(deliveryId, sujetoAnt.SUJ_IDENTIFICADOR, sujetoAnt)

    def toCommand: SujetoUpdateFromAnt =
      SujetoUpdateFromAnt(deliveryId, sujetoAnt.SUJ_IDENTIFICADOR, sujetoAnt)

  }
}
