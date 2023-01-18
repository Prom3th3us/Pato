package stubs.consumers.registrales.domicilio_sujeto

import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.{
  DomicilioSujetoAnt,
  DomicilioSujetoTri
}
import consumers.registral.domicilio_sujeto.infrastructure.json._
import stubs.loadExample

object DomicilioSujetoExternalDto {

  def domicilioSujetoTriStub: DomicilioSujetoTri =
    loadExample[DomicilioSujetoTri](
      "assets/examples/DGR-COP-DOMICILIO-SUJ-ANT.json"
    )
  def domicilioSujetoAntStub: DomicilioSujetoAnt =
    loadExample[DomicilioSujetoAnt](
      "assets/examples/DGR-COP-DOMICILIO-SUJ-TRI.json"
    )
}
