package stubs.consumers.registrales.calendario

import consumers.registral.calendario.application.entities.CalendarioExternalDto
import consumers.registral.calendario.infrastructure.json._
import stubs.loadExample

object CalendarioExternalDtoStub {
  def calendarioStub: CalendarioExternalDto =
    loadExample[CalendarioExternalDto]("assets/examples/DGR-COP-CALENDARIO.json")
}
