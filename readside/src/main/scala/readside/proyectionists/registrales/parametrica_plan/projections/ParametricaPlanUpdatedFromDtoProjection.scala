package readside.proyectionists.registrales.parametrica_plan.projections

import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto
import consumers.registral.parametrica_plan.domain.ParametricaPlanEvents

case class ParametricaPlanUpdatedFromDtoProjection(
    event: ParametricaPlanEvents.ParametricaPlanUpdatedFromDto
) extends ParametricaPlanProjection {
  val registro: ParametricaPlanExternalDto = event.registro

  def bindings: List[(String, Option[String])] = List(
    "bpp_decreto" -> registro.BPP_DECRETO
  )
}
