package consumers_spec.no_registrales.obligacion

import akka.actor.ActorSystem
import consumers_spec.no_registrales.objeto.ObjetoSpec
import consumers_spec.no_registrales.testkit.{Examples, NoRegistralesImplicitConversions}
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryTestKit
import design_principles.actor_model.ActorSpec
import design_principles.external_pub_sub.kafka.MessageProcessorLogging
import kafka.{MessageProcessor, MessageProducer}
import consumers_spec.no_registrales.testkit.MessageTestkitUtils._

object ObligacionSpec {
  case class TestContext(messageProducer: MessageProducer,
                         messageProcessor: MessageProcessor with MessageProcessorLogging,
                         Query: NoRegistralesQueryTestKit)
}
abstract class ObligacionSpec(
    getContext: ActorSystem => ObligacionSpec.TestContext
) extends ActorSpec
    with NoRegistralesImplicitConversions {
  type AggregateRoot = String
  val examples = new Examples("ObligacionSpec")

  "una obligacion" should
  "pisar una obligacion con otra nueva que llegue desde Kafka para el mismo ID" in parallelActorSystemRunner {
    implicit s =>
      val context = getContext(s)
      val messageProducer = context.messageProducer
      val Query = context.Query
      messageProducer produceObligacion examples.obligacionWithSaldo200
      eventually {
        val response = Query getStateObligacion examples.obligacionWithSaldo200
        response.saldo should be(examples.obligacionWithSaldo200.BOB_SALDO)
      }

     messageProducer produceObligacion examples.obligacionWithSaldo50
     eventually {
       val response = Query getStateObligacion examples.obligacionWithSaldo50
       response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO)
     }

  }

  "una obligacion" should "mostrar el id de juicio al recibir BOB_JUI_ID no nulo en el documento" in parallelActorSystemRunner {
    implicit s =>
      val context = getContext(s)
      val messageProducer = context.messageProducer
      val Query = context.Query
      messageProducer produceObligacion examples.obligacionWithJuicio
      eventually {
        val response = Query getStateObligacion examples.obligacionWithJuicio
        response.juicioId should contain(examples.juicioId)
      }

  }

  "una obligacion" should
  "eliminar una obligacion si llega otra nueva que llegue desde Kafka para el mismo ID y con el atributo estado con BAJA" in parallelActorSystemRunner {
    implicit s =>
      val context = getContext(s)
      val messageProducer = context.messageProducer
      val Query = context.Query
      messageProducer produceObligacion examples.obligacionWithSaldo200

      eventually {
        val response = Query getStateObligacion examples.obligacionWithSaldo200
        response.saldo should be(examples.obligacionWithSaldo200.BOB_SALDO)
      }

      messageProducer produceObligacion examples.obligacionWithSaldo200.copy(
        BOB_ESTADO = Some("BAJA")
      )

      eventually {
        val response = Query getStateObligacion examples.obligacionWithSaldo200
        response.saldo should be(0)
      }
  }

}
