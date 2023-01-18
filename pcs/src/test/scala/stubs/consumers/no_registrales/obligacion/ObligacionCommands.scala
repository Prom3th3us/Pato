package stubs.consumers.no_registrales.obligacion

import java.time.LocalDateTime

import consumers.no_registral.objeto.application.entities.ObjetoCommands.ObjetoUpdateFromObligacion
import consumers.no_registral.obligacion.application.entities.ObligacionCommands.{ObligacionUpdateExencion, _}
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.DetallesObligacion
import consumers.no_registral.obligacion.infrastructure.json._
import play.api.libs.json.Reads
import stubs.consumers.no_registrales.objeto.ObjetoExternalDto.objetoExencionStub
import stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub._
import utils.generators.Dates.{tomorrow, yesterday}
import utils.generators.Model.deliveryId

object ObligacionCommands {

  private implicit val b: Reads[Seq[DetallesObligacion]] =
    Reads.seq(DetallesObligacionF.reads)

  val detallesObligaciones: Option[Seq[DetallesObligacion]] = for {
    otrosAtributos <- obligacionesTri.BOB_OTROS_ATRIBUTOS
    bjuDetalles <- (otrosAtributos \ "BOB_DETALLES").toOption
    detalles = serialization.decodeF[Seq[DetallesObligacion]](bjuDetalles.toString)
  } yield detalles

  def obligacionUpdatedFromDtoStub =
    ObligacionUpdateFromDto(
      obligacionesTri.BOB_SUJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_IDENTIFICADOR,
      obligacionesTri.BOB_SOJ_TIPO_OBJETO,
      obligacionesTri.BOB_OBN_ID,
      deliveryId,
      obligacionesTri,
      detallesObligaciones.getOrElse(Seq.empty),
      isAdheridoDebito = None
    )
  def obigacionRemovedStub = ObligacionRemove(
    deliveryId,
    obligacionesTri.BOB_SUJ_IDENTIFICADOR,
    obligacionesTri.BOB_SOJ_IDENTIFICADOR,
    obligacionesTri.BOB_SOJ_TIPO_OBJETO,
    obligacionesTri.BOB_OBN_ID,
    registro = obligacionesTri,
    cuota = None
  )

  def updateMovimientoStub(saldo: BigDecimal, fechaVencimiento: LocalDateTime = tomorrow): ObligacionUpdateFromDto =
    updateMovimientoCompleteStub("1", "I", "1", "1", saldo.toInt, fechaVencimiento)

  def updateMovimientoStub(sujetoId: String,
                           objetoId: String,
                           obligacionId: String,
                           saldo: Int): ObligacionUpdateFromDto =
    updateMovimientoCompleteStub(objetoId, "I", sujetoId, obligacionId, saldo)

  def updateMovimientoStub(sujetoId: String,
                           objetoId: String,
                           obligacionId: String,
                           saldo: Int,
                           vencida: Boolean): ObligacionUpdateFromDto =
    vencida match {
      case true =>
        updateMovimientoCompleteStub(objetoId, "I", sujetoId, obligacionId, saldo, fechaVencimiento = yesterday)
      case false =>
        updateMovimientoCompleteStub(objetoId, "I", sujetoId, obligacionId, saldo, fechaVencimiento = tomorrow)
    }

  def updateMovimientoCompleteStub(
      objetoId: String,
      tipoObjeto: String,
      sujetoId: String,
      obligacionId: String,
      saldo: BigDecimal,
      fechaVencimiento: LocalDateTime = tomorrow,
      deliveryId: Int = deliveryId
  ): ObligacionUpdateFromDto =
    obligacionUpdatedFromDtoStub.copy(
      objetoId = objetoId,
      tipoObjeto = tipoObjeto,
      sujetoId = sujetoId,
      obligacionId = obligacionId,
      registro = obligacionesTri.copy(
        BOB_SALDO = saldo,
        BOB_VENCIMIENTO = Some(fechaVencimiento)
      ),
      deliveryId = deliveryId
    )

  implicit def toUpdateObligacionStub(a: ObjetoUpdateFromObligacion): ObligacionUpdateFromDto = {
    val o = a.obligacionId
    val sujetoId = a.sujetoId
    val objetoId = a.objetoId
    val obligacionId = a.obligacionId
    val saldo = a.saldoObligacion
    updateMovimientoStub(sujetoId, objetoId, obligacionId, saldo.toInt)
  }

  def updateExencionStub(obligacionId: String) = ObligacionUpdateExencion(
    deliveryId,
    sujetoId = objetoExencionStub.BEX_SUJ_IDENTIFICADOR,
    objetoId = objetoExencionStub.BEX_SOJ_IDENTIFICADOR,
    tipoObjeto = objetoExencionStub.BEX_SOJ_TIPO_OBJETO,
    obligacionId = obligacionId,
    exencion = objetoExencionStub
  )
}
