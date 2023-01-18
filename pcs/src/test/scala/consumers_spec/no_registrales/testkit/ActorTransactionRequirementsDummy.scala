package consumers_spec.no_registrales.testkit

import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import com.typesafe.config.ConfigFactory

object ActorTransactionRequirementsDummy {
  val requirementsDummy = ActorTransactionRequirements(
    ConfigFactory.load(),
    scala.concurrent.ExecutionContext.Implicits.global
  )
}
