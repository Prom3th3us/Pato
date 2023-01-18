package consumers_spec.no_registrales.testkit

import akka.actor.{ActorRef, ActorSystem}
import akka.entity.ShardedEntity.{MonitoringAndMessageProducer, ProductionMonitoringAndMessageProducer}
import akka.kafka.ConsumerRebalanceEvent
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.ConfigFactory
import design_principles.external_pub_sub.kafka.KafkaMock
import kafka.{KafkaMessageProducer, TopicListener}
import monitoring.{DummyMonitoring, KamonMonitoring}

case class MonitoringAndMessageProducerMock(
    monitoring: DummyMonitoring,
    messageProducer: KafkaMock
) extends MonitoringAndMessageProducer

object MonitoringAndMessageProducerMock {
  val dummy =
    MonitoringAndMessageProducerMock(
      new DummyMonitoring,
      new KafkaMock
    )
  def production(s: ActorSystem) = {
    val monitoring = new KamonMonitoring
    import akka.actor.typed.scaladsl.adapter._
    val rebalancerListener: ActorRef =
      s.spawn(
          TopicListener(
            typeKeyName = "rebalancerListener",
            monitoring
          ),
          name = "rebalancerListener"
        )
        .toClassic
    ProductionMonitoringAndMessageProducer.apply(monitoring, KafkaMessageProducer(monitoring, rebalancerListener)(s))
  }

}
