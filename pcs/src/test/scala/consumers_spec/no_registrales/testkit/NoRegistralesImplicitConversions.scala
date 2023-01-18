package consumers_spec.no_registrales.testkit

import consumers.no_registral.objeto.application.entities.ObjetoMessage.ObjetoMessageRoots
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.ObligacionesTri
import consumers.no_registral.obligacion.application.entities.ObligacionMessage.ObligacionMessageRoots
import consumers.no_registral.sujeto.application.entity.SujetoMessage.SujetoMessageRoots
import consumers_spec.no_registrales.testkit.MessageTestkitUtils.MessageProducerNoRegistrales
import consumers_spec.no_registrales.testkit.NoRegistralesImplicitConversions.ObligacionesTriRootExtractor
import kafka.MessageProducer

trait NoRegistralesImplicitConversions {

  //implicit def avoidImportOfMessageProducerNoRegistrales: MessageProducer => MessageProducerNoRegistrales =
  // messageProducer => new MessageProducerNoRegistrales(messageProducer)

  implicit def avoidImportOfObligacionesTriRootExtractor: ObligacionesTri => ObligacionesTriRootExtractor =
    obligacionTri => new ObligacionesTriRootExtractor(obligacionTri)

  implicit def toObligacionAggregateRoot: ObligacionesTri => ObligacionMessageRoots =
    obligacionTri => obligacionTri.toObligacionAggregateRoot
  implicit def toObjetoAggregateRoot: ObligacionesTri => ObjetoMessageRoots =
    obligacionTri => obligacionTri.toObjetoAggregateRoot
  implicit def toSujetoAggregateRoot: ObligacionesTri => SujetoMessageRoots =
    obligacionTri => obligacionTri.toSujetoAggregateRoot
}

object NoRegistralesImplicitConversions {

  class ObligacionesTriRootExtractor(obligacionTri: ObligacionesTri) {
    def toObligacionAggregateRoot: ObligacionMessageRoots =
      ObligacionMessageRoots(
        sujetoId = obligacionTri.BOB_SUJ_IDENTIFICADOR,
        objetoId = obligacionTri.BOB_SOJ_IDENTIFICADOR,
        tipoObjeto = obligacionTri.BOB_SOJ_TIPO_OBJETO,
        obligacionId = obligacionTri.BOB_OBN_ID
      )

    def toObjetoAggregateRoot: ObjetoMessageRoots =
      ObjetoMessageRoots(
        sujetoId = obligacionTri.BOB_SUJ_IDENTIFICADOR,
        objetoId = obligacionTri.BOB_SOJ_IDENTIFICADOR,
        tipoObjeto = obligacionTri.BOB_SOJ_TIPO_OBJETO
      )

    def toSujetoAggregateRoot: SujetoMessageRoots =
      SujetoMessageRoots(
        sujetoId = obligacionTri.BOB_SUJ_IDENTIFICADOR
      )
  }
}
