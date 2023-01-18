package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoExternalDto.DomicilioSujetoTri
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoQueries.GetStateDomicilioSujeto
import consumers.registral.domicilio_sujeto.application.entities.DomicilioSujetoResponses.GetDomicilioSujetoResponse
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoState
import consumers.registral.domicilio_sujeto.infrastructure.dependency_injection.DomicilioSujetoActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class DomicilioSujetoSpec extends TypedActorSpec {

  val actor: DomicilioSujetoActor = DomicilioSujetoActor(DomicilioSujetoState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.domicilio_sujeto.DomicilioSujetoCommands.domicilioSujetoUpdateFromDtoTriStub

    val (
      domicilio_sujetoResponse1,
      domicilio_sujetoResponse2,
      domicilio_sujetoResponse3,
      domicilio_sujetoResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "1")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioSujetoTri].copy(BDS_OBSERVACIONES = Some("FIRST REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "2")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioSujetoTri].copy(BDS_OBSERVACIONES = Some("SECOND REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "3")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioSujetoTri].copy(BDS_OBSERVACIONES = Some("THIRD REGISTRO"))
          )
      )
      domicilio_sujetoResponse1: GetDomicilioSujetoResponse <- actor.ask(
        GetStateDomicilioSujeto(sujetoId = "1", command.domicilioId)
      )
      domicilio_sujetoResponse2: GetDomicilioSujetoResponse <- actor.ask(
        GetStateDomicilioSujeto(sujetoId = "2", command.domicilioId)
      )
      domicilio_sujetoResponse3: GetDomicilioSujetoResponse <- actor.ask(
        GetStateDomicilioSujeto(sujetoId = "3", command.domicilioId)
      )
      domicilio_sujetoResponse4: GetDomicilioSujetoResponse <- actor.ask(
        GetStateDomicilioSujeto(sujetoId = "4", command.domicilioId)
      )
      _ = println(s"""
             | domicilio_sujeto 1 - $domicilio_sujetoResponse1
             | domicilio_sujeto 2 - $domicilio_sujetoResponse2
             | domicilio_sujeto 3 - $domicilio_sujetoResponse3
             |""".stripMargin)
    } yield (domicilio_sujetoResponse1,
             domicilio_sujetoResponse2,
             domicilio_sujetoResponse3,
             domicilio_sujetoResponse4)).futureValue

    domicilio_sujetoResponse1.registro.get.BDS_OBSERVACIONES.get should be("FIRST REGISTRO")
    domicilio_sujetoResponse2.registro.get.BDS_OBSERVACIONES.get should be("SECOND REGISTRO")
    domicilio_sujetoResponse3.registro.get.BDS_OBSERVACIONES.get should be("THIRD REGISTRO")
    domicilio_sujetoResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
