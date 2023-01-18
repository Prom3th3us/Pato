package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanExternalDto.ParametricaPlanTri
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanQueries.GetStateParametricaPlan
import consumers.registral.parametrica_plan.application.entities.ParametricaPlanResponses.GetParametricaPlanResponse
import consumers.registral.parametrica_plan.domain.ParametricaPlanState
import consumers.registral.parametrica_plan.infrastructure.dependency_injection.ParametricaPlanActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class ParametricaPlanSpec extends TypedActorSpec {

  val actor: ParametricaPlanActor = ParametricaPlanActor(ParametricaPlanState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.parametrica_plan.ParametricaPlanCommands.parametricaPlanUpdateFromDtoTriStub

    val (
      parametrica_planResponse1,
      parametrica_planResponse2,
      parametrica_planResponse3,
      parametrica_planResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaPlanId = "1")
          .copy(
            registro = command.registro.asInstanceOf[ParametricaPlanTri].copy(BPP_FPM_DESCRIPCION = "FIRST REGISTRO")
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaPlanId = "2")
          .copy(
            registro = command.registro.asInstanceOf[ParametricaPlanTri].copy(BPP_FPM_DESCRIPCION = "SECOND REGISTRO")
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaPlanId = "3")
          .copy(
            registro = command.registro.asInstanceOf[ParametricaPlanTri].copy(BPP_FPM_DESCRIPCION = "THIRD REGISTRO")
          )
      )
      parametrica_planResponse1: GetParametricaPlanResponse <- actor.ask(
        GetStateParametricaPlan(parametricaPlanId = "1")
      )
      parametrica_planResponse2: GetParametricaPlanResponse <- actor.ask(
        GetStateParametricaPlan(parametricaPlanId = "2")
      )
      parametrica_planResponse3: GetParametricaPlanResponse <- actor.ask(
        GetStateParametricaPlan(parametricaPlanId = "3")
      )
      parametrica_planResponse4: GetParametricaPlanResponse <- actor.ask(
        GetStateParametricaPlan(parametricaPlanId = "4")
      )
      _ = println(s"""
             | parametrica_plan 1 - $parametrica_planResponse1
             | parametrica_plan 2 - $parametrica_planResponse2
             | parametrica_plan 3 - $parametrica_planResponse3
             |""".stripMargin)
    } yield (parametrica_planResponse1,
             parametrica_planResponse2,
             parametrica_planResponse3,
             parametrica_planResponse4)).futureValue

    parametrica_planResponse1.registro.get.BPP_FPM_DESCRIPCION should be("FIRST REGISTRO")
    parametrica_planResponse2.registro.get.BPP_FPM_DESCRIPCION should be("SECOND REGISTRO")
    parametrica_planResponse3.registro.get.BPP_FPM_DESCRIPCION should be("THIRD REGISTRO")
    parametrica_planResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
