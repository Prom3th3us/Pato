package consumers_spec.no_registrales.sujeto

import akka.actor.ActorSystem
import consumers_spec.no_registrales.testkit.Examples
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryTestKit
import design_principles.actor_model.ActorSpec
import design_principles.external_pub_sub.kafka.MessageProcessorLogging
import kafka.{MessageProcessor, MessageProducer}
import utils.generators.Model.deliveryId
import consumers_spec.no_registrales.testkit.MessageTestkitUtils._
import consumers_spec.no_registrales.testkit.NoRegistralesImplicitConversions

object SujetoSpec {
  case class TestContext(messageProducer: MessageProducer,
                         messageProcessor: MessageProcessor with MessageProcessorLogging,
                         Query: NoRegistralesQueryTestKit)
}
abstract class SujetoSpec(
    getContext: ActorSystem => SujetoSpec.TestContext
) extends ActorSpec
    with NoRegistralesImplicitConversions {
  val examples = new Examples("SujetoSpec")

  "un sujeto" should
  "pisar una obligacion con otra nueva que llegue desde Kafka para el mismo ID" in parallelActorSystemRunner {
    implicit s =>
      val context = getContext(s)
      val messageProducer = context.messageProducer
      val Query = context.Query
      messageProducer produceObligacion examples.obligacionWithSaldo200
      eventually {
        val response = Query getStateSujeto examples.obligacionWithSaldo200
        response.saldo should be(examples.obligacionWithSaldo200.BOB_SALDO)
      }

      messageProducer produceObligacion examples.obligacionWithSaldo50
      eventually {
        val response = Query getStateSujeto examples.obligacionWithSaldo50
        response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO)
      }

  }

  "un sujeto" should
  "acumular saldo para diferentes objetos" in parallelActorSystemRunner { implicit s =>
    val context = getContext(s)
    val messageProducer = context.messageProducer
    val Query = context.Query
    val anotherOne = examples.obligacionWithSaldo50.copy(
      BOB_SOJ_IDENTIFICADOR = "anotherObject",
      BOB_OBN_ID = "anotherObligation",
      EV_ID = deliveryId
    )
    messageProducer produceObligacion examples.obligacionWithSaldo50
    messageProducer produceObligacion anotherOne
    eventually {
      val response = Query getStateSujeto examples.obligacionWithSaldo50
      response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO + anotherOne.BOB_SALDO)
    }

  }

  "un sujeto" should
  "Desacumular saldo a partir de la baja de un objeto" in parallelActorSystemRunner { implicit s =>
    val context = getContext(s)
    val messageProducer = context.messageProducer
    val Query = context.Query
    val anotherOne = examples.obligacionWithSaldo50.copy(
      BOB_SOJ_IDENTIFICADOR = "anotherObject",
      BOB_OBN_ID = "anotherObligation",
      EV_ID = deliveryId
    )
    messageProducer produceObligacion examples.obligacionWithSaldo50
    messageProducer produceObligacion anotherOne
    eventually {
      val response = Query getStateSujeto examples.obligacionWithSaldo50
      response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO + anotherOne.BOB_SALDO)
    }
    val anotherOneBaja = examples.objeto2.copy(
      SOJ_IDENTIFICADOR = "anotherObject",
      SOJ_ESTADO = Some("BAJA")
    )
    messageProducer produceObjeto anotherOneBaja
    eventually {
      val response = Query getStateSujeto examples.obligacionWithSaldo50
      response.objetos.size should be(1)
      response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO)
    }

  }
}
