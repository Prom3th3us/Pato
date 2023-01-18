package readside.proyectionists.registrales.exencion.projections
import java.time.chrono.ChronoLocalDateTime

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.domain.ObjetoEvents

case class ObjetoAddedExencionProjection(
    event: ObjetoEvents.ObjetoAddedExencion
) extends ExencionProjection {
  val registro: ObjetoExternalDto.Exencion = event.exencion

  def bindings = List(
    "bex_descripcion" -> registro.BEX_DESCRIPCION,
    "bex_fecha_fin" -> registro.BEX_FECHA_FIN,
    "bex_fecha_inicio" -> registro.BEX_FECHA_INICIO,
    "bex_periodo" -> registro.BEX_PERIODO,
    "bex_porcentaje " -> registro.BEX_PORCENTAJE,
    "bex_tipo" -> registro.BEX_TIPO
  )
}
