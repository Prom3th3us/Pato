package kafka

import akka.kafka.{ConsumerSettings, ProducerSettings}
import com.typesafe.config.ConfigFactory
import monitoring.Monitoring
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.concurrent.ExecutionContext

case class KafkaMessageProcessorRequirements(system: akka.actor.ActorSystem,
                                             executionContext: ExecutionContext,
                                             rebalancerListener: akka.actor.ActorRef,
                                             monitoring: Monitoring,
                                             consumer: ConsumerSettings[String, String],
                                             producer: ProducerSettings[String, String])

object KafkaMessageProcessorRequirements {

  private val config = ConfigFactory.load()
  private val appConfig = new KafkaConfig(config)
  val bootstrapServers: String = appConfig.KAFKA_BROKER

  private implicit def consumerSettings(system: akka.actor.ActorSystem): ConsumerSettings[String, String] =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withGroupId(appConfig.CONSUMER_GROUP)
      .withBootstrapServers(bootstrapServers)
      .withProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "100000")
      .withProperty(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "1")
  //.withClientId("client-new") //TODO add client ID

  private implicit def producerSettings(system: akka.actor.ActorSystem): ProducerSettings[String, String] =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  def productionSettings(rebalanceListener: akka.actor.ActorRef,
                         monitoring: Monitoring,
                         system: akka.actor.ActorSystem,
                         executionContext: ExecutionContext) =
    KafkaMessageProcessorRequirements(
      system,
      executionContext,
      rebalanceListener,
      monitoring,
      consumerSettings(system),
      producerSettings(system)
    )
}
