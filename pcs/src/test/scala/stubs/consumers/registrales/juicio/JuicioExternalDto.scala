package stubs.consumers.registrales.juicio

import consumers.registral.juicio.application.entities.JuicioExternalDto.{JuicioAnt, JuicioTri}
import consumers.registral.juicio.infrastructure.json._
import stubs.loadExample

object JuicioExternalDto {

  def juicioAntStub: JuicioAnt = loadExample[JuicioAnt]("assets/examples/DGR-COP-JUICIOS-ANT.json")
  def juicioTriStub: JuicioTri = loadExample[JuicioTri]("assets/examples/DGR-COP-JUICIOS-TRI.json")
}
