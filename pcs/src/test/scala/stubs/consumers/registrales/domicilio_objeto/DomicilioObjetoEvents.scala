package stubs.consumers.registrales.domicilio_objeto

import consumers.registral.domicilio_objeto.domain.DomicilioObjetoEvents.DomicilioObjetoUpdatedFromDto
import stubs.consumers.registrales.domicilio_objeto.DomicilioObjetoExternalDto.{
  domicilioObjetoAntStub,
  domicilioObjetoTriStub
}
import stubs.consumers.registrales.domicilio_sujeto.DomicilioSujetoExternalDto.domicilioSujetoAntStub

object DomicilioObjetoEvents {
  def domicilioObjetoUpdatedFromDtoTriStub = DomicilioObjetoUpdatedFromDto(
    deliveryId = domicilioObjetoTriStub.EV_ID.toInt,
    sujetoId = domicilioObjetoTriStub.BDO_SUJ_IDENTIFICADOR,
    objetoId = domicilioObjetoTriStub.BDO_SOJ_IDENTIFICADOR,
    tipoObjeto = domicilioObjetoTriStub.BDO_SOJ_TIPO_OBJETO,
    domicilioId = domicilioObjetoTriStub.BDO_DOM_ID,
    registro = domicilioObjetoTriStub
  )
  def domicilioObjetoUpdatedFromDtoAntStub = DomicilioObjetoUpdatedFromDto(
    deliveryId = domicilioObjetoAntStub.EV_ID.toInt,
    sujetoId = domicilioObjetoAntStub.BDO_SUJ_IDENTIFICADOR,
    objetoId = domicilioObjetoAntStub.BDO_SOJ_IDENTIFICADOR,
    tipoObjeto = domicilioObjetoAntStub.BDO_SOJ_TIPO_OBJETO,
    domicilioId = domicilioObjetoTriStub.BDO_DOM_ID,
    registro = domicilioObjetoAntStub
  )
}
