package stubs.consumers.no_registrales.sujeto

import consumers.no_registral.sujeto.application.entity.SujetoExternalDto.{SujetoAnt, SujetoTri}
import consumers.no_registral.sujeto.infrastructure.json._
import stubs.loadExample

object SujetoExternalDto {
  lazy val sujetoAntStub = loadExample[SujetoAnt]("assets/examples/DGR-COP-SUJETO-ANT.json")
  lazy val sujetoTriStub = loadExample[SujetoTri]("assets/examples/DGR-COP-SUJETO-TRI.json")
}
