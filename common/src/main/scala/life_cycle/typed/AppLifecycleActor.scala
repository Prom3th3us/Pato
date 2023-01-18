package life_cycle.typed

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}
import akka.cluster.typed.{ClusterSingleton, SingletonActor}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

private[life_cycle] object AppLifecycleActor {
  sealed trait Command
  case object Shutdown extends Command
  case class IsAppReady(replyTo: ActorRef[Boolean]) extends Command
  case object ShutdownModuleRegistry extends Command

  private val delayStopModuleRegistry = 40.seconds
  private val log = LoggerFactory.getLogger(this.getClass)
  private var state = AppLifecycleActorState()

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage[Command] {
        case Shutdown =>
          if (!state.isAppShuttingDown) {
            state = state.shutdown()
            context.scheduleOnce(delayStopModuleRegistry, context.self, ShutdownModuleRegistry)
          }
          Behaviors.same
        case IsAppReady(replyTo) =>
          replyTo ! state.isReady
          Behaviors.same
        case ShutdownModuleRegistry =>
          // @TODO shut down module registry
          log.warn("Module registry shutted down gracefully")
          Behaviors.same
      }
    }

  def init(system: ActorSystem[_]): ActorRef[AppLifecycleActor.Command] = {
    val singletonManager = ClusterSingleton(system)
    // Start if needed and provide a proxy to a named singleton
    singletonManager.init(
      SingletonActor(
        Behaviors
          .supervise(
            AppLifecycleActor()
          )
          .onFailure[Exception](SupervisorStrategy.restart),
        "AppLifecycleActorTyped"
      )
    )
  }
}
