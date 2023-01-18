package design_principles.actor_model.testsuite

import design_principles.actor_model.{ActorSpec, ActorSpecWriteside}
import design_principles.actor_model.testkit.QueryTestkit

trait ActorE2ETestSuite extends ActorSpec with ActorSpecWriteside {

  override val Query: QueryTestkit.AgainstHTTP
}
