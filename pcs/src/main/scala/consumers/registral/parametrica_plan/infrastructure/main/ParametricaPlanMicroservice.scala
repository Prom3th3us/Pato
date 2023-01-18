package consumers.registral.parametrica_plan.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.parametrica_plan.domain.ParametricaPlanState
import consumers.registral.parametrica_plan.infrastructure.dependency_injection.ParametricaPlanActor
import consumers.registral.parametrica_plan.infrastructure.http.ParametricaPlanStateAPI
import consumers.registral.parametrica_plan.infrastructure.kafka.{
  ParametricaPlanNoTributarioTransaction,
  ParametricaPlanTributarioTransaction
}
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class ParametricaPlanMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: ParametricaPlanActor = ParametricaPlanActor(ParametricaPlanState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //ParametricaPlanNoTributarioTransaction(actor, monitoring),
      ParametricaPlanTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      ParametricaPlanStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
