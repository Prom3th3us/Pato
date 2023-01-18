import com.typesafe.config.ConfigFactory
import design_principles.microservice.kafka_consumer_microservice.{KafkaConsumerMicroservice, KafkaConsumerMicroserviceRequirements}
import design_principles.microservice.kafka_consumer_microservice.MainApplication.startMicroservices


object Main extends App {

  val config = ConfigFactory.load()
  val ip = config.getString("http.ip")
  val port = config.getInt("http.port")
  val actorSystemName = "PersonClassificationService"


  startMicroservices(microservices, ip, port, actorSystemName)

  def microservices(microserviceProvisioning: KafkaConsumerMicroserviceRequirements): Seq[KafkaConsumerMicroservice] = {
    // localImplicit @deprecated | in Scala 3 we will be able to send first order functions with implicit parameters
    implicit val localImplicit: KafkaConsumerMicroserviceRequirements = microserviceProvisioning
    Seq(
      new consumers.no_registral.sujeto.infrastructure.main.SujetoMicroservice,
      new consumers.no_registral.cotitularidad.infrastructure.main.CotitularidadMicroservice,
      new consumers.no_registral.objeto.infrastructure.main.ObjetoMicroservice,
      new consumers.no_registral.obligacion.infrastructure.main.ObligacionMicroservice,
      new consumers.registral.actividad_sujeto.infrastructure.main.ActividadSujetoMicroservice,
      new consumers.registral.calendario.infrastructure.main.CalendarioMicroservice,
      //new consumers.registral.contacto.infrastructure.main.ContactoMicroservice,
      new consumers.registral.declaracion_jurada.infrastructure.main.DeclaracionJuradaMicroservice,
      new consumers.registral.domicilio_objeto.infrastructure.main.DomicilioObjetoMicroservice,
      new consumers.registral.domicilio_sujeto.infrastructure.main.DomicilioSujetoMicroservice,
      new consumers.registral.etapas_procesales.infrastructure.main.EtapasProcesalesMicroservice,
      new consumers.registral.juicio.infrastructure.main.JuicioMicroservice,
      new consumers.registral.parametrica_plan.infrastructure.main.ParametricaPlanMicroservice,
      new consumers.registral.parametrica_recargo.infrastructure.main.ParametricaRecargoMicroservice,
      new consumers.registral.plan_pago.infrastructure.main.PlanPagoMicroservice,
      new consumers.registral.subasta.infrastructure.main.SubastaMicroservice,
      new consumers.registral.tramite.infrastructure.main.TramiteMicroservice
    )
  }
}

