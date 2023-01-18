package consumers.registral.plan_pago.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.plan_pago.domain.PlanPagoState
import consumers.registral.plan_pago.infrastructure.dependency_injection.PlanPagoActor
import consumers.registral.plan_pago.infrastructure.http.PlanPagoStateAPI
import consumers.registral.plan_pago.infrastructure.kafka.{
  PlanPagoNoTributarioTransaction,
  PlanPagoTributarioTransaction
}
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class PlanPagoMicroservice(implicit m: KafkaConsumerMicroserviceRequirements) extends KafkaConsumerMicroservice {
  implicit val actor: PlanPagoActor = PlanPagoActor(PlanPagoState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //PlanPagoNoTributarioTransaction(actor, monitoring),
      PlanPagoTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      PlanPagoStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
