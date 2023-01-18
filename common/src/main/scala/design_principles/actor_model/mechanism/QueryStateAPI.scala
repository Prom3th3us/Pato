package design_principles.actor_model.mechanism

import akka.actor.{ActorRef, ActorSystem}
import akka.http.Controller
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NotFound, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Route}
import cqrs.base_actor.typed.{AbstractStateWithCQRS, BasePersistentShardedTypedActorWithCQRS}
import design_principles.actor_model.mechanism.TypedAsk.{AkkaClassicTypedAsk, AkkaTypedTypedAsk}
import design_principles.actor_model.{Query, Response}
import monitoring.Monitoring
import play.api.libs.json.{Format, Json}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

abstract class QueryStateAPI(monitoring: Monitoring) extends Controller(monitoring) {
  private val urlPrefix: Directive[Unit] = pathPrefix("state")
  def GET(route: Route): Route = (get & urlPrefix) { route }
  def POST(route: Route): Route = (post & urlPrefix) { route }
  def PATCH(route: Route): Route = (patch & urlPrefix) { route }

  def queryState[GetStateResponse <: Response: ClassTag](actorRef: ActorRef, query: Query)(
      format: Format[GetStateResponse],
      isEmpty: GetStateResponse => Boolean
  )(implicit system: ActorSystem, executionContext: ExecutionContext): Route = {
    requests.increment()
    handleErrors(exceptionHandler) {
      complete {
        val futureResponse: Future[HttpResponse] = actorRef
          .ask[Response](query)
          .map {
            case result: GetStateResponse if isEmpty(result) => HttpResponse(NotFound)

            case result: GetStateResponse =>
              HttpResponse(
                OK,
                entity = QueryStateAPI.standarization(Json.prettyPrint(format.writes(result)))
              )
          }
          .recover { case e: Exception => HttpResponse(InternalServerError, entity = e.getMessage) }
        //latency.recordFuture(futureResponse) TODO latency
        futureResponse
      }
    }
  }

  def queryState[ActorMessages <: design_principles.actor_model.ShardedMessage: ClassTag,
                 ActorEvents,
                 ActorState <: AbstractStateWithCQRS[ActorMessages, ActorEvents, ActorState],
                 Actor <: BasePersistentShardedTypedActorWithCQRS[ActorMessages, ActorEvents, ActorState],
                 QueryMessage <: ActorMessages](
      actor: BasePersistentShardedTypedActorWithCQRS[ActorMessages, ActorEvents, ActorState],
      query: QueryMessage with Query
  )(
      format: Format[QueryMessage#ReturnType],
      isEmpty: QueryMessage#ReturnType => Boolean
  )(implicit system: akka.actor.typed.ActorSystem[_]): Route = {
    implicit val ec: ExecutionContextExecutor = system.classicSystem.dispatcher
    complete {
      actor
        .ask(query)
        .map {
          case result if isEmpty(result) => HttpResponse(NotFound)

          case result =>
            HttpResponse(
              OK,
              entity = QueryStateAPI.standarization(Json.prettyPrint(format.writes(result)))
            )
        }
        .recover { case e: Exception => HttpResponse(InternalServerError, entity = e.getMessage) }
    }
  }
}

object QueryStateAPI {

  case class QueryStateApiRequirements(
      system: ActorSystem,
      executionContext: ExecutionContext
  )

  def standarization(json: String): String = {

    def camelToUnderscores(name: String) =
      "[A-Z]".r.replaceAllIn(name, { m =>
        "_" + m.group(0).toLowerCase()
      })

    /* For each class property, use the camelCase case equivalent
     * to name its column
     * (e.g. exampleFormat --> example_format)
     * (e.g. EXAMPLE_FORMAT --> example_format)
     */
    def lower_underscore(property: String): String = {
      val proposedUnderscore = camelToUnderscores(property).toLowerCase
      // in this case the string was already correct
      // and now looked like this _s_u_j__t_i_p_o
      if (proposedUnderscore contains "__") property.toLowerCase
      else proposedUnderscore
    }

    """"(\w*?)"\s?:""".r.replaceAllIn(json, { jsonKey =>
      lower_underscore(jsonKey.group(0))
    })

  }
}
