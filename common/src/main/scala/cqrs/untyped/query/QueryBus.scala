package cqrs.untyped.query

import design_principles.actor_model.Query

import scala.reflect.ClassTag

trait QueryBus[P[_]] {
  def ask[Q <: Query](query: Q): P[Q#ReturnType]

  def subscribe[Q <: Query: ClassTag](handler: Q => P[Q#ReturnType]): Unit
}
