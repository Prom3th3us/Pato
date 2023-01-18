package design_principles.actor_model

trait Command extends ShardedMessage {
  override type ReturnType = Response.SuccessProcessing
  type EventType = Event.None
  def deliveryId: BigInt
}
