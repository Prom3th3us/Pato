package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.calendario.application.entities.CalendarioQueries.GetStateCalendario
import consumers.registral.calendario.application.entities.CalendarioResponses.GetCalendarioResponse
import consumers.registral.calendario.domain.CalendarioState
import consumers.registral.calendario.infrastructure.dependency_injection.CalendarioActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class CalendarioSpec extends TypedActorSpec {

  val actor: CalendarioActor = CalendarioActor(CalendarioState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command = stubs.consumers.registrales.calendario.CalendarioCommands.calendarioUpdateFromDtoStub

    val (
      calendarioResponse1,
      calendarioResponse2,
      calendarioResponse3,
      calendarioResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(calendarioId = "1")
          .copy(registro = command.registro.copy(BCL_DESCRIPCION = Some("FIRST REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(calendarioId = "2")
          .copy(registro = command.registro.copy(BCL_DESCRIPCION = Some("SECOND REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(calendarioId = "3")
          .copy(registro = command.registro.copy(BCL_DESCRIPCION = Some("THIRD REGISTRO")))
      )
      calendarioResponse1: GetCalendarioResponse <- actor.ask(
        GetStateCalendario(calendarioId = "1")
      )
      calendarioResponse2: GetCalendarioResponse <- actor.ask(
        GetStateCalendario(calendarioId = "2")
      )
      calendarioResponse3: GetCalendarioResponse <- actor.ask(
        GetStateCalendario(calendarioId = "3")
      )
      calendarioResponse4: GetCalendarioResponse <- actor.ask(
        GetStateCalendario(calendarioId = "4")
      )
      _ = println(s"""
             | calendario 1 - $calendarioResponse1
             | calendario 2 - $calendarioResponse2
             | calendario 3 - $calendarioResponse3
             |""".stripMargin)
    } yield (calendarioResponse1, calendarioResponse2, calendarioResponse3, calendarioResponse4)).futureValue

    calendarioResponse1.registro.get.BCL_DESCRIPCION.get should be("FIRST REGISTRO")
    calendarioResponse2.registro.get.BCL_DESCRIPCION.get should be("SECOND REGISTRO")
    calendarioResponse3.registro.get.BCL_DESCRIPCION.get should be("THIRD REGISTRO")
    calendarioResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
