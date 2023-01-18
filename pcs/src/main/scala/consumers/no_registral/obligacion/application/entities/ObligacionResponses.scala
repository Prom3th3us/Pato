package consumers.no_registral.obligacion.application.entities

import java.time.LocalDateTime

import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.DetallesObligacion
import design_principles.actor_model.Response

sealed trait ObligacionResponses
object ObligacionResponses {

  case class GetObligacionResponse(
      saldo: BigDecimal = 0,
      fechaUltMod: LocalDateTime = LocalDateTime.MIN,
      registro: Option[ObligacionExternalDto] = None,
      detallesObligacion: Seq[DetallesObligacion] = Seq.empty,
      exenta: Boolean = false,
      porcentajeExencion: BigDecimal = 0,
      juicioId: Option[BigInt] = None
  ) extends Response
}
