package consumers.registral.juicio.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.juicio.domain.JuicioState
import consumers.registral.juicio.infrastructure.dependency_injection.JuicioActor
import consumers.registral.juicio.infrastructure.http.JuicioStateAPI
import consumers.registral.juicio.infrastructure.kafka.JuicioTributarioTransaction
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class JuicioMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: JuicioActor = JuicioActor(JuicioState())

  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //JuicioNoTributarioTransaction(actor, monitoring),
      JuicioTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      JuicioStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)
}
