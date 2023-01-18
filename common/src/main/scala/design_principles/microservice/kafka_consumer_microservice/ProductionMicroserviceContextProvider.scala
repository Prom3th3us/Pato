package design_principles.microservice.kafka_consumer_microservice

import akka.actor.{ActorSystem, Props}
import akka.actor.ActorRef
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.adapter._
import akka.cluster.ClusterEvent.MemberUp
import akka.http.scaladsl.server.Route
import akka.kafka.ConsumerRebalanceEvent
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import cassandra.write.CassandraWriteProduction
import com.typesafe.config.Config
import design_principles.actor_model.mechanism.QueryStateAPI.QueryStateApiRequirements
import kafka.{KafkaMessageProcessorRequirements, TopicListener}
import monitoring.KamonMonitoring

object ProductionMicroserviceContextProvider {
  def getContext(ctx: ActorSystem, config: Config)(visitor: KafkaConsumerMicroserviceRequirements => Route): Route = {

    implicit val monitoring: KamonMonitoring = new KamonMonitoring

    val rebalancerListener: ActorRef =
      ctx.actorOf(
        Props(
          new TopicListener(
            typeKeyName = "rebalancerListener",
            monitoring
          )
        )
      )

    val kafkaMessageProcessorRequirements: KafkaMessageProcessorRequirements =
      KafkaMessageProcessorRequirements.productionSettings(
        rebalancerListener,
        monitoring,
        system = ctx,
        executionContext = ctx.dispatcher
      )
    val queryStateApiRequirements = QueryStateApiRequirements(
      system = ctx,
      executionContext = ctx.dispatcher
    )
    val actorTransactionRequirements = ActorTransactionRequirements(
      executionContext = ctx.dispatcher,
      config = config
    )

    val cassandraWrite = new CassandraWriteProduction()

    visitor(
      KafkaConsumerMicroserviceRequirements(
        monitoring = monitoring,
        ctx = ctx,
        queryStateApiRequirements,
        actorTransactionRequirements,
        kafkaMessageProcessorRequirements,
        config,
        cassandraWrite
      )
    )
  }
}
