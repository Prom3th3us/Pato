package design_principles.actor_model

import cassandra.read.CassandraRead
import cassandra.write.CassandraWrite

trait ActorSpecCassandra {
  def cassandraRead: CassandraRead
  def cassandraWrite: CassandraWrite
}
