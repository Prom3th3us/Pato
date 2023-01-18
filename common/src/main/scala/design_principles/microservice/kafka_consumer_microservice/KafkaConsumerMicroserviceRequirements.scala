package design_principles.microservice.kafka_consumer_microservice

import akka.actor.ActorSystem
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.{CassandraWriteProduction}
import com.typesafe.config.Config
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import design_principles.microservice.MicroserviceRequirements
import kafka.KafkaMessageProcessorRequirements
import monitoring.{KamonMonitoring}

case class KafkaConsumerMicroserviceRequirements(
    monitoring: KamonMonitoring,
    ctx: ActorSystem,
    queryStateApiRequirements: QueryStateApiRequirements,
    actorTransactionRequirements: ActorTransactionRequirements,
    kafkaMessageProcessorRequirements: KafkaMessageProcessorRequirements,
    config: Config,
    cassandraWrite: CassandraWriteProduction
) extends MicroserviceRequirements
