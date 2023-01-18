package stubs.consumers.no_registrales

import java.time.LocalDateTime

import consumers.no_registral.objeto.application.entities.ObjetoResponses.GetObjetoResponse
import consumers.no_registral.obligacion.application.entities.ObligacionCommands.ObligacionUpdateFromDto
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{ObligacionesAnt, ObligacionesTri}
import consumers.no_registral.obligacion.application.entities.ObligacionResponses.GetObligacionResponse
import consumers.no_registral.sujeto.application.entity.SujetoResponses.GetSujetoResponse
import stubs.consumers.no_registrales.objeto.ObjetoCommands.objetoUpdateFromDtoTriStub
import utils.generators.Model.deliveryId

package object common {

  import consumers.no_registral.sujeto.infrastructure.json.GetSujetoResponseF
  implicit class GetSujetoResponsePrettyPrint(response: GetSujetoResponse) {
    def prettyPrint: String = serialization.encode(response)(GetSujetoResponseF)
  }
  import consumers.no_registral.objeto.infrastructure.json.GetObjetoResponseF
  implicit class GetObjetoResponsePrettyPrint(response: GetObjetoResponse) {
    def prettyPrint: String = serialization.encode(response)(GetObjetoResponseF)
  }
  import consumers.no_registral.obligacion.infrastructure.json.GetObligacionResponseF
  implicit class GetObligacionResponsePrettyPrint(response: GetObligacionResponse) {
    def prettyPrint: String = serialization.encode(response)(GetObligacionResponseF)
  }

  val now = LocalDateTime.now
  val tomorrow = now.plusDays(1L)
  val yesterday = now.minusDays(1L)

  val objeto1 = objetoUpdateFromDtoTriStub.copy(
    deliveryId = deliveryId,
    objetoId = "1",
    tipoObjeto = "Inmueble"
  )
  val objeto2 = objetoUpdateFromDtoTriStub.copy(
    deliveryId = deliveryId,
    objetoId = "2",
    tipoObjeto = "Automotor"
  )

  def obligacion(sujetoId: String, objetoId: String, tipoObjeto: String) =
    stubs.consumers.no_registrales.obligacion.ObligacionCommands.obligacionUpdatedFromDtoStub
      .copy(
        deliveryId = deliveryId,
        sujetoId = sujetoId,
        objetoId = objetoId,
        tipoObjeto = tipoObjeto,
        obligacionId = utils.generators.Numbers.nextInt.toString
      )

  def obligacionOfObjeto1 = obligacion("1", objeto1.objetoId, objeto1.tipoObjeto)
  def obligacionOfObjeto2 = obligacion("1", objeto2.objetoId, objeto2.tipoObjeto)
  implicit class ObligacionUpdateFromDtoTestKit(cmd: ObligacionUpdateFromDto) {

    def again =
      cmd.copy(deliveryId = deliveryId)

    def saldoNoPagado(monto: BigDecimal) =
      cmd.copy(registro = cmd.registro match {
        case registro: ObligacionesTri => registro.copy(BOB_SALDO = monto)
        case registro: ObligacionesAnt => registro.copy(BOB_SALDO = monto)
      })

    def pagada =
      cmd.copy(registro = cmd.registro match {
        case registro: ObligacionesTri => registro.copy(BOB_SALDO = 0)
        case registro: ObligacionesAnt => registro.copy(BOB_SALDO = 0)
      })
    def impaga =
      cmd.copy(registro = cmd.registro match {
        case registro: ObligacionesTri => registro.copy(BOB_SALDO = 100)
        case registro: ObligacionesAnt => registro.copy(BOB_SALDO = 100)
      })

    def noVencida =
      cmd.copy(registro = cmd.registro match {
        case registro: ObligacionesTri => registro.copy(BOB_VENCIMIENTO = Some(LocalDateTime.MAX))
        case registro: ObligacionesAnt => registro.copy(BOB_VENCIMIENTO = Some(LocalDateTime.MAX))
      })

    def venceEn(date: LocalDateTime) =
      cmd.copy(registro = cmd.registro match {
        case registro: ObligacionesTri => registro.copy(BOB_VENCIMIENTO = Some(date))
        case registro: ObligacionesAnt => registro.copy(BOB_VENCIMIENTO = Some(date))
      })

  }

}
