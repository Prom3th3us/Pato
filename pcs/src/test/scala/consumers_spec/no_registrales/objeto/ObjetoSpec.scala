package consumers_spec.no_registrales.objeto

import akka.actor.ActorSystem
import consumers_spec.no_registrales.testkit.{Examples, NoRegistralesImplicitConversions}
import utils.generators.Model.deliveryId
import consumers_spec.Utils.isObjetoBajaFromGetObjetoResponse
import consumers_spec.no_registrales.testkit.query.NoRegistralesQueryTestKit
import design_principles.actor_model.ActorSpec
import design_principles.external_pub_sub.kafka.MessageProcessorLogging
import kafka.{MessageProcessor, MessageProducer}
import consumers_spec.no_registrales.testkit.MessageTestkitUtils._

object ObjetoSpec {
  case class TestContext(messageProducer: MessageProducer,
                         messageProcessor: MessageProcessor with MessageProcessorLogging,
                         Query: NoRegistralesQueryTestKit)
}
abstract class ObjetoSpec(
    getContext: ActorSystem => ObjetoSpec.TestContext
) extends ActorSpec
    with NoRegistralesImplicitConversions {
  val examples = new Examples("ObjetoSpec")

  "un objeto" should
  "pisar una obligacion con otra nueva que llegue desde Kafka para el mismo ID" in parallelActorSystemRunner {
    implicit s =>
      val context = getContext(s)
      val messageProducer = context.messageProducer
      val Query = context.Query
      messageProducer produceObligacion examples.obligacionWithSaldo200
      eventually {
        val response = Query getStateObjeto examples.obligacionWithSaldo200
        response.saldo should be(examples.obligacionWithSaldo200.BOB_SALDO)
      }

      messageProducer produceObligacion examples.obligacionWithSaldo50
      eventually {
        val response = Query getStateObjeto examples.obligacionWithSaldo50
        response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO)
      }
  }

  "un objeto" should
  "acumular saldo para diferentes obligaciones" in parallelActorSystemRunner { implicit s =>
    val context = getContext(s)
    val messageProducer = context.messageProducer
    val Query = context.Query
    val anotherOne = examples.obligacionWithSaldo50.copy(
      BOB_OBN_ID = "anotherObligation",
      EV_ID = deliveryId
    )
    messageProducer produceObligacion examples.obligacionWithSaldo50
    messageProducer produceObligacion anotherOne
    eventually {
      val response = Query getStateObjeto examples.obligacionWithSaldo50
      response.saldo should be(examples.obligacionWithSaldo50.BOB_SALDO + anotherOne.BOB_SALDO)
    }
  }

  "un objeto" should
  "trasladar el estado baja a su state" in parallelActorSystemRunner { implicit s =>
    val context = getContext(s)
    val messageProducer = context.messageProducer
    val Query = context.Query
    messageProducer produceObjeto examples.objeto2
    eventually {
      val response = Query getStateObjeto examples.obligacionWithSaldo50
      val isBaja: Boolean = isObjetoBajaFromGetObjetoResponse(response)
      isBaja should be(false)
    }
    messageProducer produceObjeto examples.objeto2.copy(
      SOJ_ESTADO = Some("BAJA")
    )
    eventually {
      val response = Query getStateObjeto examples.obligacionWithSaldo50
      val isBaja: Boolean = isObjetoBajaFromGetObjetoResponse(response)
      isBaja should be(true)
    }
  }

  "un objeto" should
  "Restar saldo y eliminar una obligacion cuando la misma se da de baja" in parallelActorSystemRunner { implicit s =>
    val context = getContext(s)
    val messageProducer = context.messageProducer
    val Query = context.Query
    val obligacionBaja = examples.obligacionWithSaldo200.copy(BOB_OBN_ID = "obligationBaja")

    messageProducer produceObligacion obligacionBaja
    eventually {
      val response = Query getStateObjeto obligacionBaja
      response.saldo should be(obligacionBaja.BOB_SALDO)
    }

    messageProducer produceObligacion obligacionBaja.copy(BOB_ESTADO = Some("BAJA"))
    eventually {
      val response = Query getStateObjeto obligacionBaja
      response.saldo should be(0)
    }
  }

}
