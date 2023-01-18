package stubs.consumers.no_registrales.obligacion

import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{ObligacionesAnt, ObligacionesTri}
import consumers.no_registral.obligacion.infrastructure.json._
import stubs.loadExample

object ObligacionExternalDtoStub {

  lazy val obligacionesAnt = loadExample[ObligacionesAnt]("assets/examples/DGR-COP-OBLIGACIONES-ANT.json")
  lazy val obligacionesTri = loadExample[ObligacionesTri]("assets/examples/DGR-COP-OBLIGACIONES-TRI.json")

}
