package akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object AkkaHttpServer {

  case class StopAkkaHttpServer()

  def start(routes: Route, host: String, port: Int)(ctx: ActorSystem): Unit = {

    implicit val system: ActorSystem = ctx
    implicit val ec: ExecutionContext = system.dispatcher

    Http()(system)
    //.bindAndHandle(routes, host, port)
      .newServerAt(host, port)
      .bind(routes)
      .onComplete {
        case Success(bound) =>
          ctx.log.info(
            s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/"
          )
        case Failure(e) =>
          ctx.log.error("Server could not start!")
          e.printStackTrace()
      }

  }
}
