package consumers.no_registral.obligacion.infrastructure.consumer

import akka.actor.ActorRef
import api.actor_transaction.ActorTransaction
import api.actor_transaction.ActorTransaction.ActorTransactionRequirements
import consumers.no_registral.obligacion.application.entities.ObligacionCommands._
import consumers.no_registral.obligacion.application.entities.ObligacionExternalDto.{DetallesObligacion, ObligacionesTri}
import consumers.no_registral.obligacion.infrastructure.json._
import design_principles.actor_model.{Command, Response}
import monitoring.Monitoring
import oracle.Oracle.{connOracleKafkaToWriteside, connOracleNifi}
import org.slf4j.LoggerFactory
import play.api.libs.json.Reads
import serialization.maybeDecode

import scala.concurrent.Future
import scala.util.Try

case class ObligacionTributariaTransaction(actorRef: ActorRef, monitoring: Monitoring)(
  implicit
  actorTransactionRequirements: ActorTransactionRequirements
) extends ActorTransaction[ObligacionesTri](monitoring) {
  private val log = LoggerFactory.getLogger(this.getClass)
  /** Handles the deserialization of detalles de obligaciones tributarias */
  implicit val b: Reads[Seq[DetallesObligacion]] = Reads.seq(DetallesObligacionF.reads)

  def topic = "DGR-COP-OBLIGACIONES-TRI"
  def topicRetry = "DGR-COP-OBLIGACIONES-TRI_retry"
  def topicError = "DGR-COP-OBLIGACIONES-TRI_error"

  def processInput(input: String): Either[Throwable, ObligacionesTri] = {
    //connOracleNifi(input, "obligacion", "DGR-COP-OBLIGACIONES-TRI")
    maybeDecode[ObligacionesTri](input)
  }


  def processMessage(obligacion: ObligacionesTri): Future[Response.SuccessProcessing] = {
    //log.debug("KW oracle")
    //connOracleKafkaToWriteside(obligacion.EV_ID.toString(), "obligacion", obligacion.BOB_CANAL_ORIGEN.getOrElse("TAX"))
    val isAdheridoDebito = Some(obligacion.BOB_ADHERIDO_DEBITO.contains("S"))
    println(s"PEPE  -- OBLIGACION RECIVE COMMAND ${obligacion}")
    val command: Command = obligacion match {
      //this pattern match isn't  commutative
      case obn: ObligacionesTri if isCancelada(obn) =>
        println("caso 1 -- case obn: ObligacionesTri if isCancelada(obn) ")
        ObligacionRemove(
          deliveryId = obn.EV_ID,
          sujetoId = obn.BOB_SUJ_IDENTIFICADOR,
          objetoId = obn.BOB_SOJ_IDENTIFICADOR,
          tipoObjeto = obn.BOB_SOJ_TIPO_OBJETO,
          obligacionId = obn.BOB_OBN_ID,
          registro = obligacion,
          cuota = obn.BOB_CUOTA
        )
      case obn: ObligacionesTri if isNotDeuda(obn) =>
        println("caso 2 -- case obn: ObligacionesTri if isNotDeuda(obn)")
        ObligacionRemove(
          deliveryId = obn.EV_ID,
          sujetoId = obn.BOB_SUJ_IDENTIFICADOR,
          objetoId = obn.BOB_SOJ_IDENTIFICADOR,
          tipoObjeto = obn.BOB_SOJ_TIPO_OBJETO,
          obligacionId = obn.BOB_OBN_ID,
          registro = obligacion,
          cuota = None
        )
      case obn: ObligacionesTri =>
        println("caso 3 -- case obn: ObligacionesTri (wildcard)")
        ObligacionUpdateFromDto(
          sujetoId = obligacion.BOB_SUJ_IDENTIFICADOR,
          objetoId = obligacion.BOB_SOJ_IDENTIFICADOR,
          tipoObjeto = obligacion.BOB_SOJ_TIPO_OBJETO,
          obligacionId = obligacion.BOB_OBN_ID,
          deliveryId = obligacion.EV_ID,
          registro = obligacion,
          //todo: fix
          detallesObligacion = extractOtrosAtributos(obligacion).getOrElse(Seq.empty),
          isAdheridoDebito = isAdheridoDebito
        )
    }

    //return a response  to the actorRef given, this case is an ActorRef of SujetoActor
    actorRef.ask[Response.SuccessProcessing](command)
  }


  private def isNotDeuda(obligacion: ObligacionesTri): Boolean = {

    val otrosAtributos = extractOtrosAtributos(obligacion).getOrElse(Seq.empty)

    println(s"PRINTLINE otrosAtributos.nonEmpty: ${otrosAtributos.nonEmpty} ${otrosAtributos}")

    println(obligacion.BOB_ESTADO)


    val result: Boolean = (if ( otrosAtributos.nonEmpty) {

      val ruleNumber = extractRuleNumber(otrosAtributos)

      if (ruleNumber.contains("-1")) {
        true
      } else {
        false
      }
    } else {
      false
    })

    obligacion.BOB_ESTADO.contains("BAJA") || result
  }

  private def isCancelada(obligacion: ObligacionesTri): Boolean = {

    val otrosAtributos = extractOtrosAtributos(obligacion).getOrElse(default = Nil)

    val result: Boolean = (if (otrosAtributos.nonEmpty) {

      val ruleNumber = extractRuleNumber(otrosAtributos)

      if (ruleNumber.contains("-2")) {
        true
      } else {
        false
      }
    } else {
      false
    })

    result
  }

  private def extractOtrosAtributos(obn: ObligacionesTri) = {
    val detalles = for {
      otrosAtributos <- obn.BOB_OTROS_ATRIBUTOS
      bobDetalles <- (otrosAtributos \ "BOB_DETALLES").toOption
      detalles = serialization.decodeF[Seq[DetallesObligacion]](bobDetalles.toString)
    } yield (detalles)
    detalles
  }

  private def extractRuleNumber(otrosAtributos: Seq[DetallesObligacion]) = {
    otrosAtributos.headOption.flatMap(_.RULE_NUMBER)
  }

}