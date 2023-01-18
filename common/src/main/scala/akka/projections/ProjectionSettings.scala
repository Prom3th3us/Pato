package akka.projections

import akka.actor.typed.ActorSystem
import api.Utils
import com.typesafe.config.Config
import monitoring.Monitoring

object ProjectionSettings {

  def apply(system: ActorSystem[_], monitoring: Monitoring): ProjectionSettings = {
    apply(system.settings.config.getConfig("event-processor"), monitoring)
  }

  def apply(config: Config, monitoring: Monitoring): ProjectionSettings = {
    val tagPrefix: String = config.getString("tag")
    val parallelism: Int = config.getInt("parallelism")
    ProjectionSettings(tagPrefix, parallelism, monitoring)
  }

  def default(tag: String, parallelism: Int)(monitoring: Monitoring): ProjectionSettings =
    ProjectionSettings(tag, parallelism, monitoring)
}

final case class ProjectionSettings(tag: String, parallelism: Int, monitoring: Monitoring) {
  def name = tag + "Projection"
  def projectionId = Utils.Transformation.to_underscore(tag)
}
