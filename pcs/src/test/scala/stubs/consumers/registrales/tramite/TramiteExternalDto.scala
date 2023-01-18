package stubs.consumers.registrales.tramite

import consumers.registral.tramite.application.entities.TramiteExternalDto.Tramite
import consumers.registral.tramite.infrastructure.json._
import stubs.loadExample

object TramiteExternalDto {

  def tramiteStub: Tramite =
    loadExample[Tramite](
      "assets/examples/DGR-COP-TRAMITES.json"
    )
}
