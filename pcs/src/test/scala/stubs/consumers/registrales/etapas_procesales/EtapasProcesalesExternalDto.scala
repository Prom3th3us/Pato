package stubs.consumers.registrales.etapas_procesales

import consumers.registral.etapas_procesales.application.entities.EtapasProcesalesExternalDto.{
  EtapasProcesalesAnt,
  EtapasProcesalesTri
}
import consumers.registral.etapas_procesales.infrastructure.json._
import stubs.loadExample

object EtapasProcesalesExternalDto {

  def etapasProcesalesAntStub: EtapasProcesalesAnt =
    loadExample[EtapasProcesalesAnt](
      "assets/examples/DGR-COP-ETAPROCESALES-ANT.json"
    )
  def etapasProcesalesTriStub: EtapasProcesalesTri =
    loadExample[EtapasProcesalesTri](
      "assets/examples/DGR-COP-ETAPROCESALES-TRI.json"
    )
}
