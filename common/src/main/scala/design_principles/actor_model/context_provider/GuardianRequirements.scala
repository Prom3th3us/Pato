package design_principles.actor_model.context_provider

import com.typesafe.config.Config

case class GuardianRequirements(
    actorSystemName: String,
    config: Config
)
