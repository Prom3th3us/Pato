package kafka

import akka.actor.{Actor, ActorLogging}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.sharding.external._
import akka.cluster.typed.Cluster
import akka.cluster.{Cluster => CCluster}
import akka.kafka.{ConsumerRebalanceEvent, TopicPartitionsAssigned, TopicPartitionsRevoked}
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import monitoring.Monitoring

import scala.concurrent.duration._
import scala.util.Failure
import scala.util.Success

class TopicListener(typeKeyName: String, monitoring: Monitoring) extends Actor with ActorLogging {
  val TopicPartitionsRevokedCounter = monitoring.counter("TopicPartitionsRevoked")
  val TopicPartitionsAssignedCounter = monitoring.counter("TopicPartitionsAssigned")

  val system = context.system
  implicit val ec = system.dispatcher

  val shardAllocationClient = ExternalShardAllocation(system).clientFor(typeKeyName)
  system.scheduler.scheduleAtFixedRate(10.seconds, 20.seconds) { () =>
    shardAllocationClient.shardLocations().onComplete {
      case Success(shardLocations) =>
      // ctx.log.info("Current shard locations {}", shardLocations.locations)
      case Failure(t) =>
        log.error("failed to get shard locations", t)
    }
  }
  val address = CCluster(system).selfMember.address

  override def receive: Receive = {
    case TopicPartitionsAssigned(_, partitions) =>
      partitions.foreach(partition => {
        // ctx.log.info("Partition [{}] assigned to current node. Updating shard allocation", partition.partition())
        // kafka partition becomes the akka shard
        val done = shardAllocationClient.updateShardLocation(partition.partition().toString, address)
        done.onComplete { result =>
          TopicPartitionsAssignedCounter.increment()
        // ctx.log.info("Result for updating shard {}: {}", partition, result)
        }
      })
    case TopicPartitionsRevoked(_, topicPartitions) =>
      TopicPartitionsRevokedCounter.increment()
      log.warning("Partitions [{}] revoked from current node. New location will update shard allocation",
                  topicPartitions.mkString(","))

  }
}
object TopicListener {
  def apply(typeKeyName: String, monitoring: Monitoring): Behavior[ConsumerRebalanceEvent] = {
    val TopicPartitionsRevokedCounter = monitoring.counter("TopicPartitionsRevoked")
    val TopicPartitionsAssignedCounter = monitoring.counter("TopicPartitionsAssigned")
    Behaviors.setup { ctx =>
      import ctx.executionContext
      val shardAllocationClient = ExternalShardAllocation(ctx.system).clientFor(typeKeyName)
      ctx.system.scheduler.scheduleAtFixedRate(10.seconds, 20.seconds) { () =>
        shardAllocationClient.shardLocations().onComplete {
          case Success(shardLocations) =>
          // ctx.log.info("Current shard locations {}", shardLocations.locations)
          case Failure(t) =>
            ctx.log.error("failed to get shard locations", t)
        }
      }
      val address = Cluster(ctx.system).selfMember.address
      Behaviors.receiveMessage[ConsumerRebalanceEvent] {
        case TopicPartitionsAssigned(_, partitions) =>
          partitions.foreach(partition => {
            // ctx.log.info("Partition [{}] assigned to current node. Updating shard allocation", partition.partition())
            // kafka partition becomes the akka shard
            val done = shardAllocationClient.updateShardLocation(partition.partition().toString, address)
            done.onComplete { result =>
              TopicPartitionsAssignedCounter.increment()
            // ctx.log.info("Result for updating shard {}: {}", partition, result)
            }

          })
          Behaviors.same
        case TopicPartitionsRevoked(_, topicPartitions) =>
          TopicPartitionsRevokedCounter.increment()
          ctx.log.warn("Partitions [{}] revoked from current node. New location will update shard allocation",
                       topicPartitions.mkString(","))
          Behaviors.same
      }
    }
  }
}
