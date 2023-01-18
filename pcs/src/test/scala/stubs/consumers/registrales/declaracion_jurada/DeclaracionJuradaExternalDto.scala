package stubs.consumers.registrales.declaracion_jurada

import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaExternalDto.DeclaracionJurada
import consumers.registral.declaracion_jurada.infrastructure.json._
import stubs.loadExample

object DeclaracionJuradaExternalDto {

  def declaracionJuradaStub: DeclaracionJurada =
    loadExample[DeclaracionJurada](
      "assets/examples/DGR-COP-DECJURADAS.json"
    )
}
