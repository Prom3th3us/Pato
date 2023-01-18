package consumers_spec.no_registrales.testkit.query

import akka.actor.ActorSystem
import consumers.no_registral.objeto.application.entities.ObjetoMessage.ObjetoMessageRoots
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse
import consumers.no_registral.obligacion.application.entities.ObligacionMessage.ObligacionMessageRoots
import consumers.no_registral.obligacion.application.entities.ObligacionResponses.GetObligacionResponse
import consumers.no_registral.sujeto.application.entity.SujetoMessage.SujetoMessageRoots
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import design_principles.actor_model.testkit.QueryTestkit.AgainstHTTP
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContextExecutor

class NoRegistralesQueryWithHTTP()(implicit system: ActorSystem)
    extends NoRegistralesQueryTestKit
    with AgainstHTTP
    with ScalaFutures {

  import consumers.no_registral.objeto.infrastructure.json._
  import consumers.no_registral.obligacion.infrastructure.json._
  import consumers.no_registral.sujeto.infrastructure.json._
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  def getStateObligacion(obligacionExample: ObligacionMessageRoots): GetObligacionResponse = {
    val sujetoId = obligacionExample.sujetoId
    val objetoId = obligacionExample.objetoId
    val tipoObjeto = obligacionExample.tipoObjeto
    val obligacionId = obligacionExample.obligacionId
    http
      .GET[GetObligacionResponse](
        s"0.0.0.0:8081/state/sujeto/$sujetoId/objeto/$objetoId/tipo/$tipoObjeto/obligacion/$obligacionId"
      )
      .futureValue
  }

  def getStateObjeto(objetoExample: ObjetoMessageRoots): GetObjetoResponse = {
    val sujetoId = objetoExample.sujetoId
    val objetoId = objetoExample.objetoId
    val tipoObjeto = objetoExample.tipoObjeto
    http
      .GET[GetObjetoResponse](
        s"0.0.0.0:8081/state/sujeto/$sujetoId/objeto/$objetoId/tipo/$tipoObjeto"
      )
      .futureValue
  }
  def getStateSujeto(sujetoExample: SujetoMessageRoots): GetSujetoResponse = {
    val sujetoId = sujetoExample.sujetoId
    http
      .GET[GetSujetoResponse](
        s"0.0.0.0:8081/state/sujeto/$sujetoId"
      )
      .futureValue
  }
}
