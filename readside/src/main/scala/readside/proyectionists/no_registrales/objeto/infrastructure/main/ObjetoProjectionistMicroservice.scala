package readside.proyectionists.no_registrales.objeto.infrastructure.main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.actor_transaction.ActorTransaction
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}
import readside.proyectionists.no_registrales.objeto.ObjetoSnapshotPersistedHandler

class ObjetoProjectionistMicroservice(
    implicit m: KafkaConsumerMicroserviceRequirements
) extends KafkaConsumerMicroservice {

  override def actorTransactions: Set[ActorTransaction[_]] =
    Set(
      new ObjetoSnapshotPersistedHandler
    )

  override def route: Route =
    actorTransactions.map(_.route) reduce (_ ~ _)

}
