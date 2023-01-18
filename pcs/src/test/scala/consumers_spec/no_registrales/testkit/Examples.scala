package consumers_spec.no_registrales.testkit

import java.time.LocalDateTime

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.ObjetosTri
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto
import stubs.consumers.no_registrales.objeto.ObjetoExternalDto.objetoTriStub
import utils.generators.Model.deliveryId

class Examples(testName: String) {
  val sujetoId1 = s"${testName}Sujeto1"
  val sujetoId2 = s"${testName}Sujeto2"

  val objetoId2: (String, String) = ("2", "I")
  val objeto2: ObjetosTri = objetoTriStub.copy(
    EV_ID = deliveryId,
    SOJ_SUJ_IDENTIFICADOR = sujetoId1,
    SOJ_IDENTIFICADOR = objetoId2._1,
    SOJ_TIPO_OBJETO = objetoId2._2
  )

  val fechaVencimientoObligacion5: LocalDateTime = LocalDateTime.now.plusMinutes(5)
  val obligacionId = "1"
  private def obligacionExample: ObligacionExternalDto.ObligacionesTri =
    stubs.consumers.no_registrales.obligacion.ObligacionExternalDtoStub.obligacionesTri.copy(
      EV_ID = deliveryId,
      BOB_SUJ_IDENTIFICADOR = sujetoId1,
      BOB_SOJ_IDENTIFICADOR = objetoId2._1,
      BOB_SOJ_TIPO_OBJETO = objetoId2._2,
      BOB_OBN_ID = obligacionId,
      BOB_SALDO = 70,
      BOB_VENCIMIENTO = Some(fechaVencimientoObligacion5)
    )
  def obligacionWithSaldo200 =
    obligacionExample
      .copy(BOB_SALDO = 200)
      .copy(EV_ID = deliveryId)
  def obligacionWithSaldo50 =
    obligacionWithSaldo200
      .copy(BOB_SALDO = 50)
      .copy(EV_ID = deliveryId)
  def obligacionVencida =
    obligacionWithSaldo50
      .copy(BOB_VENCIMIENTO = Some(LocalDateTime.now.minusDays(1)))
      .copy(EV_ID = deliveryId)
  val juicioId = 1
  def obligacionWithJuicio =
    obligacionWithSaldo50
      .copy(BOB_JUI_ID = Some(juicioId))
      .copy(EV_ID = deliveryId)

}
