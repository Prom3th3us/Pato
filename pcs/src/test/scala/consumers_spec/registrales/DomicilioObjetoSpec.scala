package consumers_spec.registrales

import akka.Done
import config.MockConfig
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoExternalDto.DomicilioObjetoTri
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoQueries.GetStateDomicilioObjeto
import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoResponses.GetDomicilioObjetoResponse
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoState
import consumers.registral.domicilio_objeto.infrastructure.dependency_injection.DomicilioObjetoActor
import consumers_spec.Metrics
import design_principles.actor_model.{Response, TypedActorSpec}

class DomicilioObjetoSpec extends TypedActorSpec {

  val actor: DomicilioObjetoActor = DomicilioObjetoActor(DomicilioObjetoState())

  "Typed cluster sharding with persistent actor" should
  "support ask with thenReply" in {
    val command =
      stubs.consumers.registrales.domicilio_objeto.DomicilioObjetoCommands.domicilioObjetoUpdateFromDtoTriStub

    val (
      domicilio_objetoResponse1,
      domicilio_objetoResponse2,
      domicilio_objetoResponse3,
      domicilio_objetoResponse4
    ) = (for {
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "1")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioObjetoTri].copy(BDO_OBSERVACIONES = Some("FIRST REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "2")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioObjetoTri].copy(BDO_OBSERVACIONES = Some("SECOND REGISTRO"))
          )
      )
      _: Response.SuccessProcessing <- actor.ask(
        command
          .copy(sujetoId = "3")
          .copy(
            registro =
              command.registro.asInstanceOf[DomicilioObjetoTri].copy(BDO_OBSERVACIONES = Some("THIRD REGISTRO"))
          )
      )
      domicilio_objetoResponse1: GetDomicilioObjetoResponse <- actor.ask(
        GetStateDomicilioObjeto(sujetoId = "1", command.objetoId, command.tipoObjeto, command.domicilioId)
      )
      domicilio_objetoResponse2: GetDomicilioObjetoResponse <- actor.ask(
        GetStateDomicilioObjeto(sujetoId = "2", command.objetoId, command.tipoObjeto, command.domicilioId)
      )
      domicilio_objetoResponse3: GetDomicilioObjetoResponse <- actor.ask(
        GetStateDomicilioObjeto(sujetoId = "3", command.objetoId, command.tipoObjeto, command.domicilioId)
      )
      domicilio_objetoResponse4: GetDomicilioObjetoResponse <- actor.ask(
        GetStateDomicilioObjeto(sujetoId = "4", command.objetoId, command.tipoObjeto, command.domicilioId)
      )
      _ = println(s"""
             | domicilio_objeto 1 - $domicilio_objetoResponse1
             | domicilio_objeto 2 - $domicilio_objetoResponse2
             | domicilio_objeto 3 - $domicilio_objetoResponse3
             |""".stripMargin)
    } yield (domicilio_objetoResponse1,
             domicilio_objetoResponse2,
             domicilio_objetoResponse3,
             domicilio_objetoResponse4)).futureValue

    domicilio_objetoResponse1.registro.get.BDO_OBSERVACIONES.get should be("FIRST REGISTRO")
    domicilio_objetoResponse2.registro.get.BDO_OBSERVACIONES.get should be("SECOND REGISTRO")
    domicilio_objetoResponse3.registro.get.BDO_OBSERVACIONES.get should be("THIRD REGISTRO")
    domicilio_objetoResponse4.registro should be(None)

    Metrics.totalEntityCount(actor.shardActor) should be(4)
  }
}
