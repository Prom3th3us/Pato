package stubs.consumers.registrales.parametrica_recargo

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.{
  ParametricaRecargoAnt,
  ParametricaRecargoTri
}
import consumers.registral.parametrica_recargo.infrastructure.json._
import stubs.loadExample

object ParametricaRecargoExternalDto {

  def parametricaRecargoAntStub: ParametricaRecargoAnt =
    loadExample[ParametricaRecargoAnt]("assets/examples/DGR-COP-PARAMRECARGO-ANT.json")
  def parametricaRecargoTriStub: ParametricaRecargoTri =
    loadExample[ParametricaRecargoTri]("assets/examples/DGR-COP-PARAMRECARGO-TRI.json")
}
