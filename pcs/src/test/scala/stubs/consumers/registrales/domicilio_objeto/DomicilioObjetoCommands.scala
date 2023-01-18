package stubs.consumers.registrales.domicilio_objeto

import consumers.registral.domicilio_objeto.application.entities.DomicilioObjetoCommands.DomicilioObjetoUpdateFromDto
import stubs.consumers.registrales.domicilio_objeto.DomicilioObjetoExternalDto.{
  domicilioObjetoAntStub,
  domicilioObjetoTriStub
}
import utils.generators.Model.deliveryId

object DomicilioObjetoCommands {
  def domicilioObjetoUpdateFromDtoTriStub: DomicilioObjetoUpdateFromDto = DomicilioObjetoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = domicilioObjetoTriStub.BDO_SUJ_IDENTIFICADOR,
    objetoId = domicilioObjetoTriStub.BDO_SOJ_IDENTIFICADOR,
    tipoObjeto = domicilioObjetoTriStub.BDO_SOJ_TIPO_OBJETO,
    domicilioId = domicilioObjetoTriStub.BDO_DOM_ID,
    registro = domicilioObjetoTriStub
  )
  def domicilioObjetoUpdateFromDtoAntStub: DomicilioObjetoUpdateFromDto = DomicilioObjetoUpdateFromDto(
    deliveryId = deliveryId,
    sujetoId = domicilioObjetoAntStub.BDO_SUJ_IDENTIFICADOR,
    objetoId = domicilioObjetoAntStub.BDO_SOJ_IDENTIFICADOR,
    tipoObjeto = domicilioObjetoAntStub.BDO_SOJ_TIPO_OBJETO,
    domicilioId = domicilioObjetoAntStub.BDO_DOM_ID,
    registro = domicilioObjetoAntStub
  )
}
