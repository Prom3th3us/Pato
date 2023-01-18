package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.juicio.application.entities.JuicioExternalDto.JuicioTri
import consumers.registral.juicio.application.entities.JuicioQueries.GetStateJuicio
import consumers.registral.juicio.application.entities.JuicioResponses.GetJuicioResponse
import consumers.registral.juicio.domain.JuicioState
import consumers.registral.juicio.infrastructure.dependency_injection.JuicioActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class JuicioSpec extends TypedActorSpec {

  val actor: JuicioActor = JuicioActor(JuicioState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command = stubs.consumers.registrales.juicio.JuicioCommands.juicioUpdateFromDtoTriStub

    val (
      juicioResponse1,
      juicioResponse2,
      juicioResponse3,
      juicioResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "1")
          .copy(registro = command.registro.asInstanceOf[JuicioTri].copy(BJU_ESTADO = Some("FIRST REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "2")
          .copy(registro = command.registro.asInstanceOf[JuicioTri].copy(BJU_ESTADO = Some("SECOND REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "3")
          .copy(registro = command.registro.asInstanceOf[JuicioTri].copy(BJU_ESTADO = Some("THIRD REGISTRO")))
      )
      juicioResponse1: GetJuicioResponse <- actor.ask(
        GetStateJuicio(sujetoId = "1", command.objetoId, command.tipoObjeto, command.juicioId)
      )
      juicioResponse2: GetJuicioResponse <- actor.ask(
        GetStateJuicio(sujetoId = "2", command.objetoId, command.tipoObjeto, command.juicioId)
      )
      juicioResponse3: GetJuicioResponse <- actor.ask(
        GetStateJuicio(sujetoId = "3", command.objetoId, command.tipoObjeto, command.juicioId)
      )
      juicioResponse4: GetJuicioResponse <- actor.ask(
        GetStateJuicio(sujetoId = "4", command.objetoId, command.tipoObjeto, command.juicioId)
      )
      _ = println(s"""
             | juicio 1 - $juicioResponse1
             | juicio 2 - $juicioResponse2
             | juicio 3 - $juicioResponse3
             |""".stripMargin)
    } yield (juicioResponse1, juicioResponse2, juicioResponse3, juicioResponse4)).futureValue

    juicioResponse1.registro.get.BJU_ESTADO.get should be("FIRST REGISTRO")
    juicioResponse2.registro.get.BJU_ESTADO.get should be("SECOND REGISTRO")
    juicioResponse3.registro.get.BJU_ESTADO.get should be("THIRD REGISTRO")
    juicioResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
