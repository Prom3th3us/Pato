package consumers.registral.calendario.application.entities

sealed trait CalendarioCommands extends CalendarioMessage with design_principles.actor_model.Command
object CalendarioCommands {
  case class CalendarioUpdateFromDto(calendarioId: String, deliveryId: BigInt, registro: CalendarioExternalDto)
      extends CalendarioCommands

}
