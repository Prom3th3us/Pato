package generator

import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import com.typesafe.config.ConfigFactory
import generator.Generator.KafkaKeyValue
import kafka.{KafkaConfig, KafkaMessageShardProducerRecord}
import no_registrales.obligacion.{ObligacionesAntGenerator, ObligacionesTriGenerator}
import no_registrales.sujeto.{SujetoAntGenerator, SujetoTriGenerator}
import org.apache.kafka.common.serialization.StringSerializer
import registrales.actividad_sujeto.ActividadSujetoGenerator

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object KafkaEventProducer {

  def main(args: Array[String]): Unit = {

    def isInt(s: String): Boolean = s.matches("""\d+""")

    args.toList match {
      case kafkaServer :: topic :: from :: to :: Nil if isInt(from) && isInt(to) =>
        produce(kafkaServer, topic, from.toInt, to.toInt)
      case _ =>
        //     val NR_PARTITIONS: Int = Try(System.getenv("NR_PARTITIONS")).map(_.toInt).getOrElse(30)
        val kafkaServer: String = Try(System.getenv("KAFKA_SERVER")).getOrElse("0.0.0.0:9092")
        val topic: String = Try(System.getenv("KAFKA_TOPIC")).getOrElse("DGR-COP-SUJETO-TRI")
        val from: Int = Try(System.getenv("KAFKA_PRODUCE_FROM")).map(_.toInt).getOrElse(1)
        val to: Int = Try(System.getenv("KAFKA_PRODUCE_TO")).map(_.toInt).getOrElse(50000)
        produce(kafkaServer, topic, from.toInt, to.toInt)
      //     throw new IllegalArgumentException("usage: <topic> <from> <to> -- example: DGR-COP-ACTIVIDADES 1 1000")
    }
  }

  def produce(kafkaServer: String, topic: String, From: Int, To: Int): Unit = {

    implicit val system: ActorSystem = ActorSystem(
      "KafkaEventProducer",
      ConfigFactory.parseString("""
      akka.actor.provider = "local" 
     """.stripMargin).withFallback(ConfigFactory.load()).resolve()
    )

    val log = Logging(system, "KafkaEventProducer")

    val config = system.settings.config.getConfig("akka.kafka.producer")

    //TODO added NR_PARTITIONS as a config variable
    val appConfig = new KafkaConfig(ConfigFactory.load())

//    val NR_PARTITIONS: Int = appConfig.PARTITIONS_NUMBER
    val NR_PARTITIONS: Int = Try(System.getenv("NR_PARTITIONS")).map(_.toInt).getOrElse(30)

    val producerSettings: ProducerSettings[String, String] =
      ProducerSettings(config, new StringSerializer, new StringSerializer)
        .withBootstrapServers(kafkaServer)
//        .withProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "100663296") //TODO changed the buffer memory config to reduce the latency
//        .withProperty(ProducerConfig.ACKS_CONFIG, "0")
//        .withProperty(ProducerConfig.LINGER_MS_CONFIG, "5")
//        .withProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG, "120000")

    val generator: Generator[_] = topic match {
      case "DGR-COP-OBLIGACIONES-TRI" => new ObligacionesTriGenerator()
      case "DGR-COP-OBLIGACIONES-TRI1" => new ObligacionesTriGenerator()
      case "DGR-COP-OBLIGACIONES-TRI2" => new ObligacionesTriGenerator()
      case "DGR-COP-OBLIGACIONES-TRI3" => new ObligacionesTriGenerator()
      case "DGR-COP-OBLIGACIONES-ANT" => new ObligacionesAntGenerator()
      case "DGR-COP-SUJETO-TRI" => new SujetoTriGenerator()
      case "DGR-COP-SUJETO-ANT" => new SujetoAntGenerator()
      case "DGR-COP-ACTIVIDADES" => new ActividadSujetoGenerator()
    }

    def produce(keyValue: KafkaKeyValue) = {
      println(keyValue.aggregateRoot)
      KafkaMessageShardProducerRecord.producerRecord(topic, NR_PARTITIONS, keyValue.aggregateRoot, keyValue.json)
    }

    val done: Future[Done] =
      akka.stream.scaladsl.Source
        .fromIterator[Int](() => (From to To).iterator)
        // .throttle(1, 0.1 seconds)
        .map(i => generator.nextKafkaKeyValue(i))
        .map(produce)
        .runWith(Producer.plainSink(producerSettings))

    done.onComplete {
      case Success(value) =>
        log.info(s"KakfaEventProducer finished with Success($value)")
        System.exit(0)
      case Failure(exception) =>
        log.info(s"KakfaEventProducer finished with Failure($exception)")
        System.exit(1)
    }(system.dispatcher)
  }
}
