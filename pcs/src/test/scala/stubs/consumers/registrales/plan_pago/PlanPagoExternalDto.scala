package stubs.consumers.registrales.plan_pago

import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto.{PlanPagoAnt, PlanPagoTri}
import consumers.registral.plan_pago.infrastructure.json._
import stubs.loadExample

object PlanPagoExternalDto {

  def planPagoAntStub: PlanPagoAnt = loadExample[PlanPagoAnt]("assets/examples/DGR-COP-PLANES-ANT.json")
  def planPagoTriStub: PlanPagoTri = loadExample[PlanPagoTri]("assets/examples/DGR-COP-PLANES-TRI.json")
}
