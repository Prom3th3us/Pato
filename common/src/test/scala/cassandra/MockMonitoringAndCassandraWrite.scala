package cassandra

import akka.entity.ShardedEntity.MonitoringAndCassandraWrite
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWrite
import com.typesafe.config.ConfigFactory
import design_principles.projection.mock.CassandraWriteMock
import monitoring.DummyMonitoring

case class MockMonitoringAndCassandraWrite(
    monitoring: DummyMonitoring,
    cassandraWrite: CassandraWrite,
    actorTransactionRequirements: ActorTransactionRequirements
) extends MonitoringAndCassandraWrite

object MockMonitoringAndCassandraWrite {
  def apply(cassandraWriteMock: CassandraWrite): MockMonitoringAndCassandraWrite = MockMonitoringAndCassandraWrite(
    new DummyMonitoring,
    cassandraWriteMock,
    ActorTransactionRequirements(
      ConfigFactory.load(),
      scala.concurrent.ExecutionContext.Implicits.global
    )
  )
}
