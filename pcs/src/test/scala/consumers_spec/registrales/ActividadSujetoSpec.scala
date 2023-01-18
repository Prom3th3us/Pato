package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoQueries.GetStateActividadSujeto
import consumers.registral.actividad_sujeto.application.entities.ActividadSujetoResponses.GetActividadSujetoResponse
import consumers.registral.actividad_sujeto.domain.ActividadSujetoState
import consumers.registral.actividad_sujeto.infrastructure.dependency_injection.ActividadSujetoActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class ActividadSujetoSpec extends TypedActorSpec {

  val actor: ActividadSujetoActor = ActividadSujetoActor(ActividadSujetoState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.actividad_sujeto.ActividadSujetoCommands.actividadSujetoUpdateFromDtoStub

    val (
      actividadSujetoResponse1,
      actividadSujetoResponse2,
      actividadSujetoResponse3,
      actividadSujetoResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "1").copy(registro = command.registro.copy(BAT_DESCRIPCION = Some("FIRST REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "2").copy(registro = command.registro.copy(BAT_DESCRIPCION = Some("SECOND REGISTRO")))
      )
      _: Response.SuccessProcessing <- actor.ask(
        command.copy(sujetoId = "3").copy(registro = command.registro.copy(BAT_DESCRIPCION = Some("THIRD REGISTRO")))
      )
      actividadSujetoResponse1: GetActividadSujetoResponse <- actor.ask(
        GetStateActividadSujeto(sujetoId = "1", command.actividadSujetoId)
      )
      actividadSujetoResponse2: GetActividadSujetoResponse <- actor.ask(
        GetStateActividadSujeto(sujetoId = "2", command.actividadSujetoId)
      )
      actividadSujetoResponse3: GetActividadSujetoResponse <- actor.ask(
        GetStateActividadSujeto(sujetoId = "3", command.actividadSujetoId)
      )
      actividadSujetoResponse4: GetActividadSujetoResponse <- actor.ask(
        GetStateActividadSujeto(sujetoId = "4", command.actividadSujetoId)
      )
      _ = println(s"""
          | sujeto 1 - $actividadSujetoResponse1
          | sujeto 2 - $actividadSujetoResponse2
          | sujeto 3 - $actividadSujetoResponse3
          |""".stripMargin)
    } yield (actividadSujetoResponse1, actividadSujetoResponse2, actividadSujetoResponse3, actividadSujetoResponse4)).futureValue

    actividadSujetoResponse1.registro.get.BAT_DESCRIPCION.get should be("FIRST REGISTRO")
    actividadSujetoResponse2.registro.get.BAT_DESCRIPCION.get should be("SECOND REGISTRO")
    actividadSujetoResponse3.registro.get.BAT_DESCRIPCION.get should be("THIRD REGISTRO")
    actividadSujetoResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)

  }
}
