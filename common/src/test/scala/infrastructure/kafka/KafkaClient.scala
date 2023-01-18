package infrastructure.kafka

import java.time.Duration
import java.util.{Collections, Properties, UUID}

import scala.annotation.tailrec
import scala.concurrent.ExecutionException
import scala.jdk.CollectionConverters._

import akka.stream.Materializer
import com.typesafe.config.Config
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

class KafkaClient(config: Config)(implicit m: Materializer) {

  private val defaultPartitions: Int = 1
  private val defaultReplicationFactor: Short = 1

  private val consumer = KafkaClient.createConsumer(config.getString("kafka.bootstrap-servers.external"))
  private val producer = KafkaClient.createProducer(config.getString("kafka.bootstrap-servers.external"))
  private val kafkaAdminClient = KafkaClient.createAdminClient(config.getString("kafka.bootstrap-servers.external"))

  def close(): Unit = {
    producer.close()
    consumer.close()
    kafkaAdminClient.close()
  }

  def consumeTopic(topicName: String): List[String] = {
    consumer.subscribe(Collections.singletonList(topicName))
    val consumerRecords
        : ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(5000)) //TODO changed 500 to 5000
    val records =
      consumerRecords.records(topicName).asScala.map(_.value).toList
    consumer.commitSync()
    records
  }

  def createTopic(topicName: String): Unit = {
    kafkaAdminClient.createTopics(
      Collections.singletonList(new NewTopic(topicName, defaultPartitions, defaultReplicationFactor))
    )
    tryUntil(topicExists(topicName))
  }

  def listTopics(): Seq[String] =
    try {
      kafkaAdminClient.listTopics().names().get().asScala.toList.filterNot(_.startsWith("__"))
    } catch {
      case _: Throwable =>
        Seq.empty[String]
    }

  def topicExists(topic: String): Boolean = listTopics().contains(topic)

  def deleteTopic(topicName: String): Unit = {
    kafkaAdminClient.deleteTopics(Collections.singletonList(topicName))
    tryUntil(!topicExists(topicName))
  }

  def publish(topicName: String, event: String): Unit =
    publishRaw(topicName, event, 0)

  def publish(topicName: String, events: List[String]): Unit =
    events.foreach(e => publish(topicName, e))

  private val maxTries = 5
  @tailrec
  private def publishRaw(topicName: String, raw: String, tries: Int): Unit =
    if (tries <= maxTries) {
      try {
        producer.send(new ProducerRecord(topicName, 0, "key", raw)).get
      } catch {
        case _: ExecutionException =>
          createTopic(topicName)
          publishRaw(topicName, raw, tries + 1)
      }
    }

  @tailrec
  private def tryUntil(predicate: => Boolean, tries: Int = 10, sleep: Int = 200): Unit =
    if (tries > 0) {
      if (!predicate) {
        Thread.sleep(sleep)
        tryUntil(predicate, tries - 1)
      }
    }
}

object KafkaClient {
  private def createAdminClient(bootstrapServers: String): AdminClient = {
    val properties = new Properties()
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    properties.put(AdminClientConfig.RETRY_BACKOFF_MS_CONFIG, "250")
    properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "100")
    properties.put(AdminClientConfig.RETRIES_CONFIG, "3")
    properties.put(AdminClientConfig.METADATA_MAX_AGE_CONFIG, "3000")
    AdminClient.create(properties)
  }

  private def createProducer(bootstrapServers: String): KafkaProducer[String, String] = {
    val properties = new Properties()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000)
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 5)
    properties.put(ProducerConfig.LINGER_MS_CONFIG, 1000)
    new KafkaProducer[String, String](properties)
  }

  private def createConsumer(bootstrapServers: String): KafkaConsumer[String, String] = {
    val properties = new Properties()
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    new KafkaConsumer[String, String](properties)
  }
}
