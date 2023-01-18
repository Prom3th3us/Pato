package generator

import design_principles.actor_model.{Command, Event}

trait Helper {
  def toJson: String
  def toCommand: Command
  def toEvent: Event
  final def aggregateRoot: String = toCommand.aggregateRoot
}
