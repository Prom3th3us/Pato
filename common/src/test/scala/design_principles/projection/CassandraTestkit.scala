package design_principles.projection

import cassandra.read.CassandraRead
import cassandra.write.CassandraWrite

trait CassandraTestkit {
  val cassandraWrite: CassandraWrite
  val cassandraRead: CassandraRead
}
