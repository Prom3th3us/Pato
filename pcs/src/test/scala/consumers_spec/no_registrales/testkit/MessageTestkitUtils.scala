package consumers_spec.no_registrales.testkit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import akka.Done
import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.ConfigFactory
import consumers.no_registral.cotitularidad.infrastructure.kafka.ObjetoSnapshotPersistedHandler
import consumers.no_registral.objeto.application.entities.ObjetoExternalDto
import consumers.no_registral.objeto.infrastructure.consumer.{ObjetoTributarioTransaction, _}
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto
import consumers.no_registral.obligacion.infrastructure.consumer.{
  ObligacionNoTributariaTransaction,
  ObligacionTributariaTransaction
}
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto
import consumers.no_registral.sujeto.infrastructure.consumer.{
  SujetoNoTributarioTransaction,
  SujetoTributarioTransaction
}
import design_principles.actor_model.Response
import design_principles.external_pub_sub.kafka.KafkaMock.MessageProcessorImplicits
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.{MessageProcessor, MessageProducer}
import monitoring.DummyMonitoring

class MessageTestkitUtils(sujeto: ActorRef, cotitularidadActor: ActorRef) {
  implicit val actorTransactionRequirements: ActorTransactionRequirements = ActorTransactionRequirements(
    executionContext = scala.concurrent.ExecutionContext.Implicits.global,
    config = ConfigFactory.empty
  )
  implicit class StartMessageProcessor(messageBroker: MessageProcessor with MessageProducer) {
    val monitoring = new DummyMonitoring
    def startProcessing(topics: Set[ActorTransaction[_]] = Set.empty): Unit = {

      (if (topics.isEmpty) {
         println(s"[PEPE] IS EMPTY")

         Set(
           ObjetoSnapshotPersistedHandler(cotitularidadActor, monitoring),
           // ObjetoUpdateCotitularesTransaction(sujeto, monitoring),
           ObjetoTributarioTransaction(sujeto, monitoring),
           // ObjetoExencionTransaction(sujeto, monitoring),
           // ObjetoNoTributarioTransaction(sujeto, monitoring),
           // ObjetoUpdateNovedadTransaction(sujeto, monitoring),
           // ObligacionNoTributariaTransaction(sujeto, monitoring),
           ObligacionTributariaTransaction(sujeto, monitoring),
           SujetoTributarioTransaction(sujeto, monitoring)
           // SujetoNoTributarioTransaction(sujeto, monitoring)
         ) // if no filter is set, then allow passthrough
       } else topics)
        .foreach { transaction =>
          messageBroker.createTopic(transaction.topic)
          messageBroker.subscribeActorTransaction(
            transaction.topic,
            transaction
          )
        }

    }
  }

}

object MessageTestkitUtils {
  implicit class MessageProducerNoRegistrales(messageProducer: MessageProducer) {
    import consumers_spec.no_registrales.testsuite.ToJson._
    def produceObligacion(obligacion: ObligacionExternalDto): Future[akka.Done] = {
      def topic = obligacion match {
        case _: ObligacionExternalDto.ObligacionesAnt => "DGR-COP-OBLIGACIONES-ANT"
        case _: ObligacionExternalDto.ObligacionesTri => "DGR-COP-OBLIGACIONES-TRI"
      }

      messageProducer.produce(
        Seq(
          KafkaKeyValue(
            aggregateRoot =
              s"Sujeto-${obligacion.BOB_SUJ_IDENTIFICADOR}-Objeto-${obligacion.BOB_SOJ_IDENTIFICADOR}-Tipo-I-Obligacion-${obligacion.BOB_OBN_ID}",
            json = obligacion.toJson
          )
        ),
        topic
      )(_ => ())
    }

    def produceObjeto(objeto: ObjetoExternalDto): Future[akka.Done] = {
      def topic = objeto match {
        case _: ObjetoExternalDto.ObjetosAnt => "DGR-COP-OBJETOS-ANT"
        case _: ObjetoExternalDto.ObjetosTri => "DGR-COP-OBJETOS-TRI"
      }

      messageProducer.produce(
        Seq(
          KafkaKeyValue(
            aggregateRoot = s"Sujeto-${objeto.SOJ_SUJ_IDENTIFICADOR}-Objeto-${objeto.SOJ_IDENTIFICADOR}-Tipo-I",
            json = objeto.toJson
          )
        ),
        topic
      )(_ => ())
    }

    def produceSujeto(sujeto: SujetoExternalDto): Future[akka.Done] = {
      def topic = sujeto match {
        case _: SujetoExternalDto.SujetoAnt => "DGR-COP-SUJETO-ANT"
        case _: SujetoExternalDto.SujetoTri => "DGR-COP-SUJETO-TRI"
      }
      messageProducer.produce(Seq(
                                KafkaKeyValue(
                                  aggregateRoot = s"Sujeto-${sujeto.SUJ_IDENTIFICADOR}",
                                  json = sujeto.toJson
                                )
                              ),
                              topic)(_ => ())
    }
  }
}
