package cqrs.base_actor.typed

import design_principles.actor_model.mechanism.AbstractOverReplyTo.MessageWithAutomaticReplyTo

trait AbstractStateWithCQRS[Commands <: design_principles.actor_model.ShardedMessage,
                            Events,
                            State <: AbstractStateWithCQRS[Commands, Events, State]]
    extends BasePersistentShardedTypedActorAbstractState[MessageWithAutomaticReplyTo[Commands, Commands#ReturnType],
                                                         Events,
                                                         State]
