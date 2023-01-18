package design_principles.actor_model.mechanism.local_processing

import java.nio.charset.StandardCharsets

import akka.cluster.sharding.typed.ShardingMessageExtractor
import org.apache.kafka.common.utils.Utils

class LocalizedProcessingMessageExtractor[ActorMessages <: design_principles.actor_model.ShardedMessage](
    nrKafkaPartitions: Int
) extends ShardingMessageExtractor[ActorMessages, ActorMessages] {
  override def entityId(message: ActorMessages): String = message.aggregateRoot

  override def shardId(entityId: String): String = {
    LocalizedProcessingMessageExtractor.shardAndPartition(entityId, nrKafkaPartitions).toString
  }

  override def unwrapMessage(message: ActorMessages): ActorMessages = message
}

object LocalizedProcessingMessageExtractor {
  /*

        Keeping the processing local
        means that we process messages
        on the same node we receive the messages
        from Kafka.

        This gives us:
        responsive:
        - diminished latency
        resilience:
        - no networking failure point

   */
  def shardAndPartition(entityId: String, nr_partitions: Int): Int =
    Utils.toPositive(Utils.murmur2(entityId.getBytes(StandardCharsets.UTF_8))) % nr_partitions
}
