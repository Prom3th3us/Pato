package cqrs.base_actor.typed

trait BasePersistentShardedTypedActorAbstractState[
    Commands,
    Events,
    State <: BasePersistentShardedTypedActorAbstractState[Commands, Events, State]
]
