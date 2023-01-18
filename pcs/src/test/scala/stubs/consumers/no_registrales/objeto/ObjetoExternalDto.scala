package stubs.consumers.no_registrales.objeto

import java.util.concurrent.atomic.AtomicInteger
import utils.generators.Model.deliveryId
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.{Exencion, ObjetosAnt, ObjetosTri}
import consumers.no_registral.objeto.infrastructure.json._
import stubs.loadExample

object ObjetoExternalDto {

  lazy val objetoExencionStub = loadExample[Exencion]("assets/examples/DGR-COP-EXENCIONES.json")
  lazy val objetoAntStub = loadExample[ObjetosAnt]("assets/examples/DGR-COP-OBJETOS-ANT.json")
  lazy val objetoTriStub = loadExample[ObjetosTri]("assets/examples/DGR-COP-OBJETOS-TRI.json")
  lazy val objetoBajaAntStubDto = objetoAntStub.copy(SOJ_ESTADO = Some("BAJA"))
  lazy val objetoBajaTriStubDto = objetoTriStub.copy(SOJ_ESTADO = Some("BAJA"))

}
