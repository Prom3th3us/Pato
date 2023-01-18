package design_principles.actor_model

import akka.entity.ShardedEntity.Sharded

trait ShardedMessage extends Message with Sharded {
  def aggregateRoot: String
  override def entityId: String = aggregateRoot
  override def shardedId: String = aggregateRoot
  override def toString: String = aggregateRoot
}
