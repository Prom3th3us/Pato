package design_principles.actor_model

trait Response

object Response {
  case class SuccessProcessing(aggregateRoot: String, deliveryId: BigInt) extends Response

}
