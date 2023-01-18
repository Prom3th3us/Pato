package design_principles.actor_model.utils

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}
import config.StaticConfig
import serialization.EventSerializer

object Generators {
  def actorSystem(port: Int = 2559,
                  actorSystemName: String = "PersonClassificationService",
                  extraConfig: Config = ConfigFactory.empty()): ActorSystem = {
    val customConf =
      ConfigFactory.parseString(s"""
      akka.loglevel = INFO
      #akka.persistence.typed.log-stashing = on
      akka.actor.provider = cluster
      akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
      akka.persistence.journal.inmem.test-serialization = on
      akka.actor.allow-java-serialization = true
      akka.cluster.jmx.multi-mbeans-in-same-jvm = on

      akka.cluster.seed-nodes = ["akka://$actorSystemName@0.0.0.0:$port"]
      akka.remote.artery.canonical.port = $port

      """)
    lazy val config: Config = Seq(
      extraConfig, // Level 0 - Max priority
      customConf, // Level 1 - will replace values set in StaticConfig
      StaticConfig.config // Level 2 - this is the base config. Ready for overrides.
    ).reduce(_ withFallback _)

    ActorSystem(actorSystemName, config)
  }
}
