package design_principles.microservice.kafka_consumer_microservice

import api.actor_transaction.{ActorTransaction, ActorTransactionController}
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import design_principles.microservice.Microservice
import kafka.{KafkaMessageProcessorRequirements, KafkaMessageProducer}
import monitoring.{KamonMonitoring}
import akka.actor.typed.scaladsl.adapter._
import akka.entity.ShardedEntity.{ProductionMonitoringAndCassandraWrite, ProductionMonitoringAndMessageProducer}
import design_principles.actor_model.mechanism.stream_supervision.UniqueTopicPerNode.uniqueTopicPerNode

abstract class KafkaConsumerMicroservice(implicit m: KafkaConsumerMicroserviceRequirements)
    extends Microservice[KafkaConsumerMicroserviceRequirements] {

  implicit final val classicSystem: akka.actor.ActorSystem = m.ctx
  implicit final val system: akka.actor.typed.ActorSystem[Nothing] = m.ctx.toTyped

  implicit final val monitoring: KamonMonitoring = m.monitoring
  implicit final val queryStateApiR: QueryStateApiRequirements = m.queryStateApiRequirements
  implicit final val kafkaMessageProcessorR: KafkaMessageProcessorRequirements = m.kafkaMessageProcessorRequirements
  implicit final val actorTransactionR: ActorTransaction.ActorTransactionRequirements = m.actorTransactionRequirements
  implicit val messageProducer: KafkaMessageProducer =
    KafkaMessageProducer(monitoring, m.kafkaMessageProcessorRequirements.rebalancerListener)
  implicit final val monitoringAndMessageProducer: ProductionMonitoringAndMessageProducer =
    ProductionMonitoringAndMessageProducer(
      monitoring,
      messageProducer
    )
  implicit final val monitoringAndCassandraWrite: ProductionMonitoringAndCassandraWrite =
    ProductionMonitoringAndCassandraWrite(
      monitoring,
      m.cassandraWrite,
      actorTransactionR
    )

  def actorTransactions: Set[ActorTransaction[_]]

  final def actorTransactionControllers: Set[(String, ActorTransactionController)] =
    actorTransactions.map { actorTransaction =>
      (uniqueTopicPerNode(actorTransaction.topic), actorTransaction.controller)
    }
}
