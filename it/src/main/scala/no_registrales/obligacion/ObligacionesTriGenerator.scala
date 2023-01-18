package no_registrales.obligacion

import java.util.concurrent.atomic.AtomicInteger

import consumers.no_registral.obligacion.application.entities.ObligacionCommands.ObligacionUpdateFromDto
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{
  DetallesObligacion,
  ObligacionesAnt,
  ObligacionesTri
}
import consumers.no_registral.obligacion.domain.ObligacionEvents.ObligacionUpdatedFromDto
import consumers.no_registral.obligacion.infrastructure.json.DetallesObligacionF
import generator.Helper
import play.api.libs.json.Reads
import consumers.no_registral.obligacion.infrastructure.json._
import generator.Generator.{deliveryId, loadExample}
import generator.{Generator, Helper}
import play.api.libs.json.JsPath.\

import scala.util.Random

class ObligacionesTriGenerator extends Generator[ObligacionesTri] {
  import ObligacionesTriGenerator._
  lazy val example: ObligacionesTri = loadExample[ObligacionesTri]("/assets/examples/DGR-COP-OBLIGACIONES-TRI.json")

  def next(id: Int): ObligacionesTri =
    example.copy(
      EV_ID = id,
      BOB_SUJ_IDENTIFICADOR = id.toString,
      BOB_SOJ_IDENTIFICADOR = id.toString,
      BOB_OBN_ID = id.toString
    )

  override def toJson(e: ObligacionesTri): String = e.toJson

  override def aggregateRoot(e: ObligacionesTri): String = e.aggregateRoot

}

object ObligacionesTriGenerator {
  implicit class ObligacionesTriGeneratorHelper(obligacionesTri: ObligacionesTri) extends Helper {

    def toJson: String =
      serialization.encode(obligacionesTri)

    def toEvent: ObligacionUpdatedFromDto = {
      ObligacionUpdatedFromDto(
        deliveryId,
        obligacionesTri.BOB_SUJ_IDENTIFICADOR,
        obligacionesTri.BOB_SOJ_IDENTIFICADOR,
        obligacionesTri.BOB_SOJ_TIPO_OBJETO,
        obligacionesTri.BOB_OBN_ID,
        obligacionesTri,
        detallesObligaciones.getOrElse(Seq.empty),
        Some(true)
      )
    }

    def toCommand: ObligacionUpdateFromDto = {
      implicit val b: Reads[Seq[DetallesObligacion]] =
        Reads.seq(DetallesObligacionF.reads)

      def obligacionUpdatedFromDtoStub =
        ObligacionUpdateFromDto(
          obligacionesTri.BOB_SUJ_IDENTIFICADOR,
          obligacionesTri.BOB_SOJ_IDENTIFICADOR,
          obligacionesTri.BOB_SOJ_TIPO_OBJETO,
          obligacionesTri.BOB_OBN_ID,
          deliveryId,
          obligacionesTri,
          detallesObligaciones.getOrElse(Seq.empty),
          Some(true)
        )

      obligacionUpdatedFromDtoStub
    }

    val detallesObligaciones: Option[Seq[DetallesObligacion]] = for {
      otrosAtributos <- obligacionesTri.BOB_OTROS_ATRIBUTOS
      bjuDetalles <- (otrosAtributos \ "BOB_DETALLES").toOption
      detalles = serialization.decodeF[Seq[DetallesObligacion]](bjuDetalles.toString)
    } yield detalles

  }

}
