package consumers.no_registral.obligacion.infrastructure.main

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.no_registral.obligacion.infrastructure.consumer.{ObligacionNoTributariaTransaction, ObligacionTributariaTransaction, ObligacionTributariaTransaction2, ObligacionTributariaTransaction3, ObligacionTributariaTransactionAutomotor, ObligacionTributariaTransactionBilletera, ObligacionTributariaTransactionCuotaPlan, ObligacionTributariaTransactionEmbarcacion, ObligacionTributariaTransactionIngresoBruto, ObligacionTributariaTransactionInmueble, ObligacionTributariaTransactionJuicio, ObligacionTributariaTransactionMultiobjeto}
import consumers.no_registral.obligacion.infrastructure.http.ObligacionStateAPI
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import design_principles.microservice.kafka_consumer_microservice.{KafkaConsumerMicroservice, KafkaConsumerMicroserviceRequirements}

class ObligacionMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {

  implicit val actor: ActorRef = SujetoActor.startWithRequirements(monitoringAndMessageProducer)

  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      ObligacionTributariaTransaction(actor, monitoring),

      ObligacionTributariaTransactionBilletera(actor, monitoring),
      ObligacionTributariaTransactionInmueble(actor, monitoring),
      ObligacionTributariaTransactionAutomotor(actor, monitoring),
      ObligacionTributariaTransactionIngresoBruto(actor, monitoring),
      ObligacionTributariaTransactionMultiobjeto(actor, monitoring),
      ObligacionTributariaTransactionEmbarcacion(actor, monitoring),
      ObligacionTributariaTransactionCuotaPlan(actor, monitoring),
      ObligacionTributariaTransactionJuicio(actor, monitoring),
      ObligacionTributariaTransaction2(actor, monitoring),
      ObligacionTributariaTransaction3(actor, monitoring),
      ObligacionNoTributariaTransaction(actor, monitoring)
    )

  def route: Route = {
    (Seq(
      ObligacionStateAPI(actor, monitoring).route
    ) ++
    actorTransactions.map(_.route).toSeq) reduce (_ ~ _)
  }

}


