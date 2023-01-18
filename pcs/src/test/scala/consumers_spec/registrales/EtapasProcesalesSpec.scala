package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto.EtapasProcesalesTri
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesQueries.GetStateEtapasProcesales
import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesResponses.GetEtapasProcesalesResponse
import consumers.registral.etapas_procesales.domain.EtapasProcesalesState
import consumers.registral.etapas_procesales.infrastructure.dependency_injection.EtapasProcesalesActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class EtapasProcesalesSpec extends TypedActorSpec {

  val actor: EtapasProcesalesActor = EtapasProcesalesActor(EtapasProcesalesState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.etapas_procesales.EtapasProcesalesCommands.etapasProcesalesUpdateFromDtoTriStub

    val (
      etapas_procesalesResponse1,
      etapas_procesalesResponse2,
      etapas_procesalesResponse3,
      etapas_procesalesResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(juicioId = "1")
          .copy(
            registro = command.registro.asInstanceOf[EtapasProcesalesTri].copy(BEP_DESCRIPCION = Some("FIRST REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(juicioId = "2")
          .copy(
            registro =
              command.registro.asInstanceOf[EtapasProcesalesTri].copy(BEP_DESCRIPCION = Some("SECOND REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(juicioId = "3")
          .copy(
            registro = command.registro.asInstanceOf[EtapasProcesalesTri].copy(BEP_DESCRIPCION = Some("THIRD REGISTRO"))
          )
      )
      etapas_procesalesResponse1: GetEtapasProcesalesResponse <- actor.ask(
        GetStateEtapasProcesales(juicioId = "1", command.etapaId)
      )
      etapas_procesalesResponse2: GetEtapasProcesalesResponse <- actor.ask(
        GetStateEtapasProcesales(juicioId = "2", command.etapaId)
      )
      etapas_procesalesResponse3: GetEtapasProcesalesResponse <- actor.ask(
        GetStateEtapasProcesales(juicioId = "3", command.etapaId)
      )
      etapas_procesalesResponse4: GetEtapasProcesalesResponse <- actor.ask(
        GetStateEtapasProcesales(juicioId = "4", command.etapaId)
      )
      _ = println(s"""
             | etapas_procesales 1 - $etapas_procesalesResponse1
             | etapas_procesales 2 - $etapas_procesalesResponse2
             | etapas_procesales 3 - $etapas_procesalesResponse3
             |""".stripMargin)
    } yield (etapas_procesalesResponse1,
             etapas_procesalesResponse2,
             etapas_procesalesResponse3,
             etapas_procesalesResponse4)).futureValue

    etapas_procesalesResponse1.registro.get.BEP_DESCRIPCION.get should be("FIRST REGISTRO")
    etapas_procesalesResponse2.registro.get.BEP_DESCRIPCION.get should be("SECOND REGISTRO")
    etapas_procesalesResponse3.registro.get.BEP_DESCRIPCION.get should be("THIRD REGISTRO")
    etapas_procesalesResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
