package no_registrales.sujeto

import consumers.no_registral.sujeto.application.entity.SujetoCommands.SujetoUpdateFromTri
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.SujetoTri
import consumers.no_registral.sujeto.domain.SujetoEvents.SujetoUpdatedFromTri
import consumers.no_registral.sujeto.infrastructure.json._
import generator.Generator.{deliveryId, loadExample}
import generator.{Generator, Helper}

class SujetoTriGenerator extends Generator[SujetoTri] {
  import SujetoTriGenerator._

  lazy val example = loadExample[SujetoTri]("/assets/examples/DGR-COP-SUJETO-TRI.json")

  def next(id: Int): SujetoTri =
    example.copy(
      EV_ID = id,
      SUJ_IDENTIFICADOR = id.toString
    )

  override def toJson(e: SujetoTri): String = e.toJson

  override def aggregateRoot(e: SujetoTri): String = e.aggregateRoot
}

object SujetoTriGenerator {
  implicit class SujetoTriGeneratorHelper(sujetoTri: SujetoTri) extends Helper {

    def toJson: String =
      serialization.encode(sujetoTri)

    def toEvent: SujetoUpdatedFromTri =
      SujetoUpdatedFromTri(deliveryId, sujetoTri.SUJ_IDENTIFICADOR, sujetoTri)

    def toCommand: SujetoUpdateFromTri =
      SujetoUpdateFromTri(deliveryId, sujetoTri.SUJ_IDENTIFICADOR, sujetoTri)

  }
}
