package stubs.consumers.registrales.actividad_sujeto

import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoExternalDto.ActividadSujeto
import consumers.registral.actividad_sujeto.infrastructure.json._
import stubs.loadExample

object ActividadSujetoExternalDto {
  def actividadSujetoStub: ActividadSujeto =
    loadExample[ActividadSujeto](
      "assets/examples/DGR-COP-ACTIVIDADES.json"
    )
}
