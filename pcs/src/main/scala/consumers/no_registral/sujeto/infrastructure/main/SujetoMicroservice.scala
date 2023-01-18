package consumers.no_registral.sujeto.infrastructure.main

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.no_registral.sujeto.infrastructure.consumer.{SujetoNoTributarioTransaction, SujetoTributarioTransaction}
import consumers.no_registral.sujeto.infrastructure.dependency_injection.SujetoActor
import consumers.no_registral.sujeto.infrastructure.http.SujetoStateAPI
import design_principles.microservice.kafka_consumer_microservice.{KafkaConsumerMicroservice, KafkaConsumerMicroserviceRequirements}

class SujetoMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: ActorRef = SujetoActor.startWithRequirements(monitoringAndMessageProducer)

  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      SujetoTributarioTransaction(actor, monitoring),
      SujetoNoTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      SujetoStateAPI(actor, monitoring).route
    ) ++
    actorTransactions.map(_.route).toSeq)
      .reduce(_ ~ _)
}
