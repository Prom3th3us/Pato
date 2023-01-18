package design_principles.actor_model.testkit

import akka.actor.{ActorRef, ActorSystem}
import design_principles.actor_model.Query
import infrastructure.http.HttpClient
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Format

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

trait QueryTestkit {
  type QueryTypeUpperBound
  def ask[QueryType <: QueryTypeUpperBound, ReturnType: ClassTag](query: QueryType)(
      implicit system: ActorSystem,
      format: Format[ReturnType]
  ): ReturnType
}

object QueryTestkit {
  trait AgainstActors extends QueryTestkit with ScalaFutures {
    def actor: ActorRef
    override type QueryTypeUpperBound = Query
    override def ask[QueryType <: QueryTypeUpperBound, ReturnType: ClassTag](
        query: QueryType
    )(
        implicit system: ActorSystem,
        format: Format[ReturnType]
    ): ReturnType =
      actor.ask(query).futureValue
  }

  trait AgainstHTTP extends QueryTestkit with ScalaFutures {
    val http = new HttpClient()

    override final type QueryTypeUpperBound = String

    override def ask[QueryType <: QueryTypeUpperBound, ReturnType: ClassTag](
        query: QueryType
    )(implicit system: ActorSystem, format: Format[ReturnType]): ReturnType = {
      implicit val ec: ExecutionContextExecutor = system.dispatcher
      http.GET[ReturnType](query).futureValue
    }
  }
}
