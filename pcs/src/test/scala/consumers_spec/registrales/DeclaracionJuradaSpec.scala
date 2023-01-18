package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaQueries.GetStateDeclaracionJurada
import consumers.registral.declaracion_jurada.application.entities.DeclaracionJuradaResponses.GetDeclaracionJuradaResponse
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaState
import consumers.registral.declaracion_jurada.infrastructure.dependency_injection.DeclaracionJuradaActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class DeclaracionJuradaSpec extends TypedActorSpec {

  val actor = DeclaracionJuradaActor(DeclaracionJuradaState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.declaracion_jurada.DeclaracionJuradaCommands.declaracionJuradaUpdateFromDtoStub

    val (
      declaracionJuradaResponse1,
      declaracionJuradaResponse2,
      declaracionJuradaResponse3,
      declaracionJuradaResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "1").copy(registro = command.registro.copy(BDJ_ESTADO = Some("FIRST REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "2").copy(registro = command.registro.copy(BDJ_ESTADO = Some("SECOND REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "3").copy(registro = command.registro.copy(BDJ_ESTADO = Some("THIRD REGISTRO")))
      )
      getDeclaracionJuradaResponse1: GetDeclaracionJuradaResponse <- actor.ask(
        GetStateDeclaracionJurada(sujetoId = "1", command.objetoId, command.tipoObjeto, command.declaracionJuradaId)
      )
      getDeclaracionJuradaResponse2: GetDeclaracionJuradaResponse <- actor.ask(
        GetStateDeclaracionJurada(sujetoId = "2", command.objetoId, command.tipoObjeto, command.declaracionJuradaId)
      )
      getDeclaracionJuradaResponse3: GetDeclaracionJuradaResponse <- actor.ask(
        GetStateDeclaracionJurada(sujetoId = "3", command.objetoId, command.tipoObjeto, command.declaracionJuradaId)
      )
      getDeclaracionJuradaResponse4: GetDeclaracionJuradaResponse <- actor.ask(
        GetStateDeclaracionJurada(sujetoId = "4", command.objetoId, command.tipoObjeto, command.declaracionJuradaId)
      )
      _ = println(s"""
             | sujeto 1 - $getDeclaracionJuradaResponse1
             | sujeto 2 - $getDeclaracionJuradaResponse2
             | sujeto 3 - $getDeclaracionJuradaResponse3
             |""".stripMargin)
    } yield (getDeclaracionJuradaResponse1,
             getDeclaracionJuradaResponse2,
             getDeclaracionJuradaResponse3,
             getDeclaracionJuradaResponse4)).futureValue

    declaracionJuradaResponse1.registro.get.BDJ_ESTADO.get should be("FIRST REGISTRO")
    declaracionJuradaResponse2.registro.get.BDJ_ESTADO.get should be("SECOND REGISTRO")
    declaracionJuradaResponse3.registro.get.BDJ_ESTADO.get should be("THIRD REGISTRO")
    declaracionJuradaResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)

  }
}
