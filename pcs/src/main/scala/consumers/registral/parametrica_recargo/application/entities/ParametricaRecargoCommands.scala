package consumers.registral.parametrica_recargo.application.entities

sealed trait ParametricaRecargoCommands extends ParametricaRecargoMessage with design_principles.actor_model.Command
object ParametricaRecargoCommands {
  case class ParametricaRecargoUpdateFromDto(parametricaRecargoId: String,
                                             deliveryId: BigInt,
                                             registro: ParametricaRecargoExternalDto)
      extends ParametricaRecargoCommands

}
