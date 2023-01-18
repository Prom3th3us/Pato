package readside.proyectionists.registrales.parametrica_recargo.projections

import java.time.chrono.ChronoLocalDateTime

import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoEvents

case class ParametricaRecargoUpdatedFromDtoProjection(
    event: ParametricaRecargoEvents.ParametricaRecargoUpdatedFromDto
) extends ParametricaRecargoProjection {
  val registro: ParametricaRecargoExternalDto = event.registro

  def bindings = List(
    "bpr_descripcion" -> registro.BPR_DESCRIPCION,
    "bpr_fecha_hasta" -> registro.BPR_FECHA_HASTA,
    "bpr_valor" -> registro.BPR_VALOR
  )
}
