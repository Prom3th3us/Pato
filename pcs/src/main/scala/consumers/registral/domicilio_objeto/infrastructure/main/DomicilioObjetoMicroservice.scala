package consumers.registral.domicilio_objeto.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.domicilio_objeto.domain.DomicilioObjetoState
import consumers.registral.domicilio_objeto.infrastructure.dependency_injection.DomicilioObjetoActor
import consumers.registral.domicilio_objeto.infrastructure.http.DomicilioObjetoStateAPI
import consumers.registral.domicilio_objeto.infrastructure.kafka.DomicilioObjetoTributarioTransaction
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class DomicilioObjetoMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: DomicilioObjetoActor = DomicilioObjetoActor(DomicilioObjetoState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //DomicilioObjetoNoTributarioTransaction(actor, monitoring),
      DomicilioObjetoTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      DomicilioObjetoStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
