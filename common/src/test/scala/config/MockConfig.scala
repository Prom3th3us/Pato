package config

import akka.dispatchers.ActorsDispatchers
import com.typesafe.config.ConfigFactory
import serialization.EventSerializer

object MockConfig {
  private val mainConfig = ConfigFactory.load()
  lazy val config = Seq(
    mainConfig,
    ConfigFactory parseString EventSerializer.eventAdapterConf,
    ConfigFactory parseString EventSerializer.serializationConf,
    ConfigFactory parseString new ActorsDispatchers(mainConfig).actorsDispatchers
  ).reduce(_ withFallback _)
}
