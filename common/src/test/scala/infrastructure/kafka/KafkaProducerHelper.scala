package infrastructure.kafka

import java.util.Properties
import java.util.concurrent.ExecutionException
import com.typesafe.config.Config
import infrastructure.kafka.zk.{AdminZkClient, KafkaZkClient}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.utils.Time
import play.api.libs.json.{Json, Writes}

class KafkaProducerHelper(config: Config) {

  private val maxTries = 5

  private val properties = new Properties()
  properties.put("bootstrap.servers", config.getString("bootstrap-servers"))
  properties.put("key.serializer", classOf[StringSerializer])
  properties.put("value.serializer", classOf[StringSerializer])
  properties.put("max.block.ms", config.getString("max-block-ms"))
  private val producer = new KafkaProducer[String, String](properties)

  private val zkClient = KafkaZkClient(
    config.getString("zookeeper-host"),
    isSecure = false,
    sessionTimeoutMs = 2000,
    connectionTimeoutMs = 15000,
    maxInFlightRequests = 10,
    time = Time.SYSTEM
  )
  private val adminZkClient = new AdminZkClient(zkClient)

  def publish[Event: Writes](topic: String, event: Event): Unit =
    publishRaw(topic, Json.toJson(event).toString(), 0)

  def publishRaw(topic: String, raw: String): Unit = publishRaw(topic, raw, 0)

  def publishRaw(topic: String, raw: String, tries: Int): Unit =
    if (tries <= maxTries) {
      try {
        producer.send(new ProducerRecord(topic, 0, "key", raw)).get
      } catch {
        case _: ExecutionException =>
          createTopic(topic)
          Thread.sleep(1000)
          publishRaw(topic, raw, tries + 1)
      }
    }

  def close(): Unit =
    producer.close()

  def deleteTopic(topic: String): Unit =
    adminZkClient.deleteTopic(topic)

  def createTopic(topic: String): Unit =
    adminZkClient.createTopic(topic, 1, 1)
}
