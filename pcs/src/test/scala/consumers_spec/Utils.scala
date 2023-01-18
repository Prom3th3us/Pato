package consumers_spec

import akka.actor.ActorRef
import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse
import design_principles.actor_model.mechanism.TypedAsk.AkkaClassicTypedAsk
import design_principles.actor_model.{Command, Query, Response}
import org.scalatest.Assertion
import utils.generators.Model.deliveryId

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

object Utils {

  def actorInteraction[Response: ClassTag](command: Command, query: Query)(
      assertion: Response => Assertion
  )(implicit actorRef: ActorRef, ec: ExecutionContext): Future[Assertion] =
    for {
      _ <- actorRef.ask[Response.SuccessProcessing](command)
      response <- actorRef.ask[Response](query)
    } yield assertion(response)

  def uniqueActorName: String = s"MockSujeto-${deliveryId}"

  def isObjetoBajaFromGetObjetoResponse(response: GetObjetoResponse): Boolean = response match {
    case response: GetObjetoResponse =>
      response.registro match {
        case Some(registro) if registro.SOJ_ESTADO.contains("BAJA") => true
        case Some(registro) if !registro.SOJ_ESTADO.contains("BAJA") => false
      }
  }

}
