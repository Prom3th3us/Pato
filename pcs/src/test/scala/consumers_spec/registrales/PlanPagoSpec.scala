package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.plan_pago.application.entities.PlanPagoExternalDto.PlanPagoTri
import consumers.registral.plan_pago.application.entities.PlanPagoQueries.GetStatePlanPago
import consumers.registral.plan_pago.application.entities.PlanPagoResponses.GetPlanPagoResponse
import consumers.registral.plan_pago.domain.PlanPagoState
import consumers.registral.plan_pago.infrastructure.dependency_injection.PlanPagoActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class PlanPagoSpec extends TypedActorSpec {

  val actor: PlanPagoActor = PlanPagoActor(PlanPagoState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command = stubs.consumers.registrales.plan_pago.PlanPagoCommands.planPagoUpdateFromDtoTriStub

    val (
      plan_pagoResponse1,
      plan_pagoResponse2,
      plan_pagoResponse3,
      plan_pagoResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "1")
          .copy(registro = command.registro.asInstanceOf[PlanPagoTri].copy(BPL_ESTADO = Some("FIRST REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "2")
          .copy(registro = command.registro.asInstanceOf[PlanPagoTri].copy(BPL_ESTADO = Some("SECOND REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "3")
          .copy(registro = command.registro.asInstanceOf[PlanPagoTri].copy(BPL_ESTADO = Some("THIRD REGISTRO")))
      )
      plan_pagoResponse1: GetPlanPagoResponse <- actor.ask(
        GetStatePlanPago(sujetoId = "1", command.objetoId, command.tipoObjeto, command.planPagoId)
      )
      plan_pagoResponse2: GetPlanPagoResponse <- actor.ask(
        GetStatePlanPago(sujetoId = "2", command.objetoId, command.tipoObjeto, command.planPagoId)
      )
      plan_pagoResponse3: GetPlanPagoResponse <- actor.ask(
        GetStatePlanPago(sujetoId = "3", command.objetoId, command.tipoObjeto, command.planPagoId)
      )
      plan_pagoResponse4: GetPlanPagoResponse <- actor.ask(
        GetStatePlanPago(sujetoId = "4", command.objetoId, command.tipoObjeto, command.planPagoId)
      )
      _ = println(s"""
             | plan_pago 1 - $plan_pagoResponse1
             | plan_pago 2 - $plan_pagoResponse2
             | plan_pago 3 - $plan_pagoResponse3
             |""".stripMargin)
    } yield (plan_pagoResponse1, plan_pagoResponse2, plan_pagoResponse3, plan_pagoResponse4)).futureValue

    plan_pagoResponse1.registro.get.BPL_ESTADO.get should be("FIRST REGISTRO")
    plan_pagoResponse2.registro.get.BPL_ESTADO.get should be("SECOND REGISTRO")
    plan_pagoResponse3.registro.get.BPL_ESTADO.get should be("THIRD REGISTRO")
    plan_pagoResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
