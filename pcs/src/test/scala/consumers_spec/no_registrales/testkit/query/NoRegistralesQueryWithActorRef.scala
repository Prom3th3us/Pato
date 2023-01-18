package consumers_spec.no_registrales.testkit.query

import akka.actor.ActorRef
import consumers.no_registral.objeto.application.entities.ObjetoMessage.ObjetoMessageRoots
import consumers.no_registral.objeto.application.entities.ObjetoQueries.GetStateObjeto
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse
import consumers.no_registral.obligacion.application.entities.ObligacionMessage.ObligacionMessageRoots
import consumers.no_registral.obligacion.application.entities.ObligacionQueries.GetStateObligacion
import consumers.no_registral.obligacion.application.entities.ObligacionResponses.GetObligacionResponse
import consumers.no_registral.sujeto.application.entity.SujetoMessage.SujetoMessageRoots
import consumers.no_registral.sujeto.application.entity.SujetoQueries.GetStateSujeto
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import design_principles.actor_model.testkit.QueryTestkit.AgainstActors
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

class NoRegistralesQueryWithActorRef(sujeto: ActorRef)
    extends NoRegistralesQueryTestKit
    with AgainstActors
    with ScalaFutures
    with IntegrationPatience {

  val actor: ActorRef = sujeto

  def getStateObligacion(obligacionExample: ObligacionMessageRoots): GetObligacionResponse =
    sujeto
      .ask[GetObligacionResponse](
        GetStateObligacion(
          obligacionExample.sujetoId,
          obligacionExample.objetoId,
          obligacionExample.tipoObjeto,
          obligacionExample.obligacionId
        )
      )
      .futureValue
  def getStateObjeto(objetoExample: ObjetoMessageRoots): GetObjetoResponse =
    sujeto
      .ask[GetObjetoResponse](
        GetStateObjeto(
          objetoExample.sujetoId,
          objetoExample.objetoId,
          objetoExample.tipoObjeto
        )
      )
      .futureValue
  def getStateSujeto(sujetoExample: SujetoMessageRoots): GetSujetoResponse =
    sujeto
      .ask[GetSujetoResponse](
        GetStateSujeto(
          sujetoExample.sujetoId
        )
      )
      .futureValue
}
