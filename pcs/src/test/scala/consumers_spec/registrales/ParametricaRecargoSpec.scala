package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoExternalDto.ParametricaRecargoTri
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoQueries.GetStateParametricaRecargo
import consumers.registral.parametrica_recargo.application.entities.ParametricaRecargoResponses.GetParametricaRecargoResponse
import consumers.registral.parametrica_recargo.domain.ParametricaRecargoState
import consumers.registral.parametrica_recargo.infrastructure.dependency_injection.ParametricaRecargoActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class ParametricaRecargoSpec extends TypedActorSpec {

  val actor: ParametricaRecargoActor = ParametricaRecargoActor(ParametricaRecargoState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.parametrica_recargo.ParametricaRecargoCommands.parametricaPlanUpdateFromDtoTriStub

    val (
      parametrica_recargoResponse1,
      parametrica_recargoResponse2,
      parametrica_recargoResponse3,
      parametrica_recargoResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaRecargoId = "1")
          .copy(registro = command.registro.asInstanceOf[ParametricaRecargoTri].copy(BPR_CONCEPTO = "FIRST REGISTRO"))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaRecargoId = "2")
          .copy(
            registro = command.registro.asInstanceOf[ParametricaRecargoTri].copy(BPR_CONCEPTO = "SECOND REGISTRO")
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(parametricaRecargoId = "3")
          .copy(registro = command.registro.asInstanceOf[ParametricaRecargoTri].copy(BPR_CONCEPTO = "THIRD REGISTRO"))
      )
      parametrica_recargoResponse1: GetParametricaRecargoResponse <- actor.ask(
        GetStateParametricaRecargo(parametricaRecargoId = "1")
      )
      parametrica_recargoResponse2: GetParametricaRecargoResponse <- actor.ask(
        GetStateParametricaRecargo(parametricaRecargoId = "2")
      )
      parametrica_recargoResponse3: GetParametricaRecargoResponse <- actor.ask(
        GetStateParametricaRecargo(parametricaRecargoId = "3")
      )
      parametrica_recargoResponse4: GetParametricaRecargoResponse <- actor.ask(
        GetStateParametricaRecargo(parametricaRecargoId = "4")
      )
      _ = println(s"""
             | parametrica_recargo 1 - $parametrica_recargoResponse1
             | parametrica_recargo 2 - $parametrica_recargoResponse2
             | parametrica_recargo 3 - $parametrica_recargoResponse3
             |""".stripMargin)
    } yield (parametrica_recargoResponse1,
             parametrica_recargoResponse2,
             parametrica_recargoResponse3,
             parametrica_recargoResponse4)).futureValue

    parametrica_recargoResponse1.registro.get.BPR_CONCEPTO should be("FIRST REGISTRO")
    parametrica_recargoResponse2.registro.get.BPR_CONCEPTO should be("SECOND REGISTRO")
    parametrica_recargoResponse3.registro.get.BPR_CONCEPTO should be("THIRD REGISTRO")
    parametrica_recargoResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
