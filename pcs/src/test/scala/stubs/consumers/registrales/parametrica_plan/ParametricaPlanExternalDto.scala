package stubs.consumers.registrales.parametrica_plan

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto.{
  ParametricaPlanAnt,
  ParametricaPlanTri
}
import consumers.registral.parametrica_plan.infrastructure.json._
import stubs.loadExample

object ParametricaPlanExternalDto {

  def parametricaPlanAntStub: ParametricaPlanAnt =
    loadExample[ParametricaPlanAnt](
      "assets/examples/DGR-COP-PARAMPLAN-ANT.json"
    )
  def parametricaPlanTriStub: ParametricaPlanTri =
    loadExample[ParametricaPlanTri](
      "assets/examples/DGR-COP-PARAMPLAN-TRI.json"
    )
}
