package stubs.consumers.registrales.domicilio_objeto

import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto.{
  DomicilioObjetoAnt,
  DomicilioObjetoTri
}
import consumers.registral.domicilio_objeto.infrastructure.json._
import stubs.loadExample

object DomicilioObjetoExternalDto {

  def domicilioObjetoAntStub: DomicilioObjetoAnt =
    loadExample[DomicilioObjetoAnt](
      "assets/examples/DGR-COP-DOMICILIO-OBJ-ANT.json"
    )

  def domicilioObjetoTriStub: DomicilioObjetoTri =
    loadExample[DomicilioObjetoTri](
      "assets/examples/DGR-COP-DOMICILIO-OBJ-TRI.json"
    )
}
