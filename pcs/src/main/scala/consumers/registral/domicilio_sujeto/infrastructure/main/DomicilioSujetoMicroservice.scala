package consumers.registral.domicilio_sujeto.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.domicilio_sujeto.domain.DomicilioSujetoState
import consumers.registral.domicilio_sujeto.infrastructure.dependency_injection.DomicilioSujetoActor
import consumers.registral.domicilio_sujeto.infrastructure.http.DomicilioSujetoStateAPI
import consumers.registral.domicilio_sujeto.infrastructure.kafka.{
  DomicilioSujetoNoTributarioTransaction,
  DomicilioSujetoTributarioTransaction
}
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class DomicilioSujetoMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: DomicilioSujetoActor = DomicilioSujetoActor(DomicilioSujetoState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //DomicilioSujetoNoTributarioTransaction(actor, monitoring),
      DomicilioSujetoTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      DomicilioSujetoStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
