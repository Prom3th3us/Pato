package design_principles.actor_model.mechanism.stream_supervision

import com.typesafe.config.ConfigFactory

object UniqueTopicPerNode {

  private val config = ConfigFactory.load()
  private val ip = config.getString("http.CLUSTER_IP")
  private val port = config.getString("http.port")
  // Topic formatter
  def uniqueTopicPerNode(topic: String) = s"$topic|$ip:$port"
}
