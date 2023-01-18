package config

import akka.dispatchers.ActorsDispatchers
import com.typesafe.config.ConfigFactory
import serialization.EventSerializer

object StaticConfig {
  private val mainConfig = ConfigFactory.load()
  lazy val config = Seq(
    mainConfig,
    ConfigFactory parseString EventSerializer.eventAdapterConf,
    ConfigFactory parseString EventSerializer.serializationConf,
    //TODO  verificar
    ConfigFactory parseString new ActorsDispatchers(mainConfig).actorsDispatchers
    //ConfigFactory parseString StrongScaling.apply(mainConfig).strongScalingDispatcherCassandra
  ).reduce(_ withFallback _)
}
