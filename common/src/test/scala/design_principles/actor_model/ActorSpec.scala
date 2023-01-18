package design_principles.actor_model

import scala.concurrent.duration._
import scala.util.Try
import akka.actor.ActorSystem
import akka.pattern.ask
import com.typesafe.config.{Config, ConfigFactory}
import design_principles.actor_model.system_parallelizable.ActorSystemGenerator.RunTest
import design_principles.actor_model.system_parallelizable.{ActorSystemParallelizerBuilder, AvailablePortProvider}
import design_principles.actor_model.utils.Generators
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, RandomTestOrder}
import serialization.EventSerializer

object ActorSpec {
  lazy val config: Config = Seq(
    ConfigFactory parseString EventSerializer.eventAdapterConf,
    ConfigFactory parseString EventSerializer.serializationConf,
    ConfigFactory.load()
  ).reduce(_ withFallback _)

  def system: ActorSystem = {
    val availablePort = AvailablePortProvider.port
    Generators.actorSystem(availablePort)
  }
}

abstract class ActorSpec
    extends AnyFlatSpecLike
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Eventually
    with IntegrationPatience
    with RandomTestOrder
    with ScalaFutures {

  def parallelActorSystemRunner(testContext: ActorSystem => Unit): Unit =
    ActorSystemParallelizerBuilder.actor
      .ask(RunTest(testContext))(2.minutes)
      .mapTo[Try[Unit]]
      .futureValue(timeout(2.minutes))
      .get
}
