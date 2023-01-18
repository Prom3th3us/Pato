package stubs.consumers.registrales.subasta

import consumers.registral.subasta.application.entities.SubastaExternalDto
import consumers.registral.subasta.infrastructure.json._
import stubs.loadExample

object SubastaExternalDtoStub {

  def subastaStub: SubastaExternalDto =
    loadExample[SubastaExternalDto](
      "assets/examples/DGR-COP-SUBASTAS.json"
    )
}
