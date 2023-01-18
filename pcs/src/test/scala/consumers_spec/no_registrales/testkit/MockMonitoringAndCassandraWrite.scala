package consumers_spec.no_registrales.testkit

object MockMonitoringAndCassandraWrite {}
import akka.entity.ShardedEntity.MonitoringAndCassandraWrite
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.read.CassandraRead
import cassandra.write.CassandraWrite
//import design_principles.projection.mock.CassandraTestkitMock.ProyectionistReaction
import design_principles.projection.mock.{CassandraTestkitMock, CassandraWriteMock}
import infrastructure.cassandra.CassandraTestkit.CassandraTestkit
import monitoring.DummyMonitoring
/*
case class MockMonitoringAndCassandraWrite(
    monitoring: DummyMonitoring,
    cassandraWrite: CassandraTestkitMock,
    actorTransactionRequirements: ActorTransactionRequirements
) extends MonitoringAndCassandraWrite

object MockMonitoringAndCassandraWrite {
  def dummy(
      proyectionistReaction: ProyectionistReaction
  ) = MockMonitoringAndCassandraWrite(
    new DummyMonitoring,
    new CassandraTestkitMock(proyectionistReaction),
    ActorTransactionRequirementsDummy.requirementsDummy
  )
}
 */
