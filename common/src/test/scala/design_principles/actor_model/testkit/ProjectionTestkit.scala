package design_principles.actor_model.testkit

trait ProjectionTestkit

object ProjectionTestkit {
  trait AgainstCassandra extends ProjectionTestkit
  trait AgainstCassandraMock extends ProjectionTestkit
}
