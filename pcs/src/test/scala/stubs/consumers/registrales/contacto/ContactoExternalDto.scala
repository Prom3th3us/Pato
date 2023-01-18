package stubs.consumers.registrales.contacto

import consumers.registral.contacto.application.json._
import stubs.loadExample

object ContactoExternalDto {

  def contactoStub =
    loadExample[consumers.registral.contacto.domain.ContactoExternalDto]("assets/examples/DGR-COP-CONTACTO.json")

}
