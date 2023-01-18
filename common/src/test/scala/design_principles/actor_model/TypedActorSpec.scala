package design_principles.actor_model

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.cluster.typed.{Cluster, Join}
import com.typesafe.config.{Config, ConfigFactory}
import cqrs.base_actor.typed.{AbstractStateWithCQRS, BasePersistentShardedTypedActorWithCQRS}
import design_principles.actor_model.mechanism.TypedAsk
import design_principles.actor_model.mechanism.TypedAsk.AkkaTypedTypedAsk
import design_principles.external_pub_sub.kafka.KafkaMock
import kafka.{KafkaMessageProducer, MessageProducer}
import org.scalatest.flatspec.AnyFlatSpecLike

abstract class TypedActorSpec extends ScalaTestWithActorTestKit(TypedActorSpec.config) with AnyFlatSpecLike {

  implicit def typedAsk[ActorMessages <: ShardedMessage: ClassTag,
                        ActorEvents,
                        State <: AbstractStateWithCQRS[ActorMessages, ActorEvents, State]](
      actor: BasePersistentShardedTypedActorWithCQRS[
        ActorMessages,
        ActorEvents,
        State
      ]
  ): TypedAsk.AkkaTypedTypedAsk[ActorMessages, ActorEvents, State] = new AkkaTypedTypedAsk(actor)

  implicit val ec: ExecutionContextExecutor = system.classicSystem.dispatcher
  implicit val messageProducer: MessageProducer = new KafkaMock()
  Cluster(system).manager ! Join(Cluster(system).selfMember.address)
}

object TypedActorSpec {
  val config: Config = ConfigFactory.parseString("""
      akka.loglevel = INFO
      #akka.persistence.typed.log-stashing = on
      akka.actor.provider = cluster
      akka.remote.classic.netty.tcp.port = 0
      akka.remote.artery.canonical.port = 0
      akka.remote.artery.canonical.hostname = 127.0.0.1
      akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
      akka.persistence.journal.inmem.test-serialization = on
      akka.actor.allow-java-serialization = true
      akka.cluster.jmx.multi-mbeans-in-same-jvm = on
    """)
}
