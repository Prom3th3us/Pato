package cqrs.untyped.command

import design_principles.actor_model.Command

import scala.reflect.ClassTag

trait CommandBus[P[_]] {
  def publish[C <: Command](command: C): P[C#ReturnType]

  def subscribe[C <: Command: ClassTag](handler: C => P[C#ReturnType]): Unit
}
