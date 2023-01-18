package consumers.registral.etapas_procesales.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import consumers.registral.etapas_procesales.domain.EtapasProcesalesState
import consumers.registral.etapas_procesales.infrastructure.dependency_injection.EtapasProcesalesActor
import consumers.registral.etapas_procesales.infrastructure.http.EtapasProcesalesStateAPI
import consumers.registral.etapas_procesales.infrastructure.kafka.{
  EtapasProcesalesNoTributarioTransaction,
  EtapasProcesalesTributarioTransaction
}
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}

class EtapasProcesalesMicroservice(implicit m: KafkaConsumerMicroserviceRequirements)
    extends KafkaConsumerMicroservice {
  implicit val actor: EtapasProcesalesActor = EtapasProcesalesActor(EtapasProcesalesState())
  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      //EtapasProcesalesNoTributarioTransaction(actor, monitoring),
      EtapasProcesalesTributarioTransaction(actor, monitoring)
    )

  override def route: Route =
    (Seq(
      EtapasProcesalesStateAPI(actor, monitoring).route
    ) ++ actorTransactions.map(_.route)) reduce (_ ~ _)

}
