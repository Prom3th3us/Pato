package akka.projections

import com.typesafe.config.{Config, ConfigFactory}
import config.StaticConfig

import scala.util.Try

object ProjectionHandlerConfig {

  def getThisTagParallelism(tag: String): Int = getThisTagParallelism(StaticConfig.config)(tag)
  def getThisTagParallelism(config: Config)(tag: String): Int = {

    val projectionHandler = utils.Inference.getSimpleName(this.getClass.getName)

    val parallelism: Int = Try {
      config.getInt(
        s"projectionists.$tag.paralellism"
      )
    }.getOrElse {
      Try {
        config
          .getString(
            s"projectionists.$tag.paralellism"
          )
          .toInt
      }.getOrElse(1)
    }
    parallelism
  }

}
