package readside

import com.typesafe.config.{Config, ConfigFactory}
import design_principles.microservice.kafka_consumer_microservice.{
  KafkaConsumerMicroservice,
  KafkaConsumerMicroserviceRequirements
}
import design_principles.microservice.kafka_consumer_microservice.MainApplication.startMicroservices
import design_principles.microservice.kafka_consumer_microservice.KafkaConsumerMicroserviceRequirements
import monitoring.KamonMonitoring

object Main extends App {

  private val config: Config = ConfigFactory.load()
  val ip = config.getString("http.ip")
  val port = config.getInt("http.port")
  val actorSystemName = "PersonClassificationServiceReadSide"

  startMicroservices(microservices, ip, port, actorSystemName)

  def microservices(
      microserviceProvisioning: KafkaConsumerMicroserviceRequirements
  ): Seq[KafkaConsumerMicroservice] = {
    // localImplicit @deprecated | in Scala 3 we will be able to send first order functions with implicit parameters
    implicit val localImplicit: KafkaConsumerMicroserviceRequirements = microserviceProvisioning
    Seq(
      new readside.proyectionists.no_registrales.obligacion.infrastructure.main.ObligacionProjectionistMicroservice,
      new readside.proyectionists.no_registrales.objeto.infrastructure.main.ObjetoProjectionistMicroservice,
      new readside.proyectionists.no_registrales.sujeto.infrastructure.main.SujetoProjectionistMicroservice,
      new readside.proyectionists.registrales.actividad_sujeto.infrastructure.main.ActividadSujetoProjectionistMicroservice,
      new readside.proyectionists.registrales.declaracion_jurada.infrastructure.main.DeclaracionJuradaProjectionistMicroservice,
      new readside.proyectionists.registrales.domicilio_objeto.infrastructure.main.DomicilioObjetoProjectionistMicroservice,
      new readside.proyectionists.registrales.domicilio_sujeto.infrastructure.main.DomicilioSujetoProjectionistMicroservice,
      new readside.proyectionists.registrales.etapas_procesales.infrastructure.main.EtapasProcesalesProjectionistMicroservice,
      new readside.proyectionists.registrales.exencion.infrastructure.main.ExencionProjectionistMicroservice,
      new readside.proyectionists.registrales.juicio.infrastructure.main.JuicioProjectionistMicroservice,
      new readside.proyectionists.registrales.parametrica_plan.infrastructure.main.ParametricaPlanProjectionistMicroservice,
      new readside.proyectionists.registrales.parametrica_recargo.infrastructure.main.ParametricaRecargoProjectionistMicroservice,
      new readside.proyectionists.registrales.plan_pago.infrastructure.main.PlanPagoProjectionistMicroservice,
      new readside.proyectionists.registrales.subasta.infrastructure.main.SubastaProjectionistMicroservice,
      new readside.proyectionists.registrales.tramite.infrastructure.main.TramiteProjectionistMicroservice
    )
  }
}
