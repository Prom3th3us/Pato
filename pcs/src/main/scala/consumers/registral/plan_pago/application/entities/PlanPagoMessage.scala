package consumers.registral.plan_pago.application.entities

import consumers.no_registral.objeto.application.entities.ObjetoMessage
import consumers.no_registral.sujeto.application.entity.SujetoMessage
import consumers.registral.plan_pago.application.entities.PlanPagoMessage.PlanPagoMessageRoots

trait PlanPagoMessage extends SujetoMessage with ObjetoMessage {

  val planPagoId: String

  override def aggregateRoot: String =
    PlanPagoMessageRoots(
      sujetoId,
      objetoId,
      tipoObjeto,
      planPagoId
    ).toString
}

object PlanPagoMessage {

  case class PlanPagoMessageRoots(sujetoId: String, objetoId: String, tipoObjeto: String, planPagoId: String) {
    override def toString = s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-PlanPago-$planPagoId"
  }
  object PlanPagoMessageRoots {

    def extractor(persistenceId: String): PlanPagoMessageRoots =
      persistenceId match {
        case s"Sujeto-$sujetoId-Objeto-$objetoId-$tipoObjeto-PlanPago-$planPagoId" =>
          PlanPagoMessageRoots(sujetoId, objetoId, tipoObjeto, planPagoId)
      }
  }
}
