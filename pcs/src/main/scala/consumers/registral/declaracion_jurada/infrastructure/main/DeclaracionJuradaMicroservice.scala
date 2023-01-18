package consumers.registral.declaracion_jurada.infrastructure.main

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.declaracion_jurada.domain.DeclaracionJuradaState
import consumers.registral.declaracion_jurada.infrastructure.dependency_injection.DeclaracionJuradaActor
import consumers.registral.declaracion_jurada.infrastructure.http.DeclaracionJuradaStateAPI
import consumers.registral.declaracion_jurada.infrastructure.kafka.DeclaracionJuradaTransaction
import design_principles.actor_model.mechanism.tell_supervision.TellSupervisor
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}
import akka.actor.typed.scaladsl.adapter._

class DeclaracionJuradaMicroservice(implicit m: KafkaConsumerMicroserviceRequirements)
    extends KafkaConsumerMicroservice {
  implicit val actor: DeclaracionJuradaActor = DeclaracionJuradaActor(DeclaracionJuradaState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(DeclaracionJuradaTransaction(actor, monitoring))

  override def route: Route =
    (Seq(
      DeclaracionJuradaStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
