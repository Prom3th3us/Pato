package consumers.registral.parametrica_plan.application.entities

sealed trait ParametricaPlanCommands extends ParametricaPlanMessage with design_principles.actor_model.Command
object ParametricaPlanCommands {
  case class ParametricaPlanUpdateFromDto(parametricaPlanId: String,
                                          deliveryId: BigInt,
                                          registro: ParametricaPlanExternalDto)
      extends ParametricaPlanCommands

}
