package consumers.registral.plan_pago.application.entities

sealed trait PlanPagoCommands extends design_principles.actor_model.Command with PlanPagoMessage
object PlanPagoCommands {
  case class PlanPagoUpdateFromDto(objetoId: String,
                                   tipoObjeto: String,
                                   planPagoId: String,
                                   deliveryId: BigInt,
                                   sujetoId: String,
                                   registro: PlanPagoExternalDto)
      extends PlanPagoCommands

}
