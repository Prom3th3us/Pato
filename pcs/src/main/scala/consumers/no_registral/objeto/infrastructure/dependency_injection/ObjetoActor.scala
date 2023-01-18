package consumers.no_registral.objeto.infrastructure.dependency_injection

import akka.ActorRefMap
import akka.actor.{ActorRef, Props}
import akka.entity.ShardedEntity.MonitoringAndMessageProducer
import consumers.no_registral.objeto.application.cqrs.commands._
import consumers.no_registral.objeto.application.cqrs.queries.{
  GetSnapshotObjetoHandler,
  GetStateExencionHandler,
  GetStateObjetoHandler
}
import consumers.no_registral.objeto.application.entities.ObjetoMessage.ObjetoMessageRoots
import consumers.no_registral.objeto.application.entities.ObjetoQueries.GetSnapshotObjeto
import consumers.no_registral.objeto.application.entities.{ObjetoCommands, ObjetoQueries}
import consumers.no_registral.objeto.domain.ObjetoEvents.ObjetoSnapshotPersisted
import consumers.no_registral.objeto.domain.{ObjetoEvents, ObjetoState}
import consumers.no_registral.obligacion.application.entities.ObligacionCommands._
import consumers.no_registral.obligacion.application.entities.ObligacionMessage._
import consumers.no_registral.obligacion.application.entities.{ObligacionCommands, ObligacionMessage}
import consumers.no_registral.obligacion.domain.ObligacionEvents
import consumers.no_registral.obligacion.infrastructure.dependency_injection.ObligacionActor
import consumers.no_registral.sujeto.application.entity.SujetoCommands
import cqrs.base_actor.untyped.PersistentBaseActor
import kafka.KafkaMessageProducer.KafkaKeyValue
import kafka.MessageProducer

class ObjetoActor(requirements: MonitoringAndMessageProducer, obligacionActorPropsOption: Option[Props] = None)
    extends PersistentBaseActor[ObjetoEvents, ObjetoState](requirements.monitoring) {
  import ObjetoActor._

  var state = ObjetoState()
  implicit val messageProducer: MessageProducer = requirements.messageProducer

  println(s"[PEPE] OBJETO started $persistenceId")
  logger.error(s"[PEPE] OBJETO started $persistenceId")

  override def setupHandlers(): Unit = {
    commandBus.subscribe[ObjetoCommands.ObjetoSnapshot](new ObjetoSnapshotHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoTagAdd](new ObjetoTagAddHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoTagRemove](new ObjetoTagRemoveHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoUpdateFromAnt](new ObjetoUpdateFromAntHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoUpdateFromTri](new ObjetoUpdateFromTriHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.SetBajaObjeto](new SetBajaObjetoHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoUpdateFromObligacion](new ObjetoUpdateFromObligacionHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoUpdateCotitulares](new ObjetoUpdateCotitularesHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoAddExencion](new ObjetoAddExencionHandler(this).handle)
    commandBus.subscribe[ObjetoCommands.ObjetoRemoveObligacion](new ObjetoRemoveObligacionHandler(this).handle)
    queryBus.subscribe[ObjetoQueries.GetStateObjeto](new GetStateObjetoHandler(this).handle)
    queryBus.subscribe[ObjetoQueries.GetStateExencion](new GetStateExencionHandler(this).handle)
    queryBus.subscribe[ObjetoQueries.GetSnapshotObjeto](new GetSnapshotObjetoHandler(this).handle)
  }

  val obligaciones: ObjetoActorRefMap = {
    val obligacionActorProps = obligacionActorPropsOption match {
      case Some(props) => props
      case None => ObligacionActor.props(requirements)
    }
    new ObjetoActorRefMap(
      {
        case (sujetoId, objetoId, tipoObjeto, obligacionId) =>
          val obligacionAggregateRoot = ObligacionMessageRoots(
            sujetoId,
            objetoId,
            tipoObjeto,
            obligacionId
          ).toString()
          context.actorOf(obligacionActorProps, obligacionAggregateRoot)

        case other => context.actorOf(obligacionActorProps, other toString)
      }
    )
  }

  override def receiveCommand: Receive = customReceiveCommand orElse super.receiveCommand
  override def receiveRecover: Receive = customReceiveRecover orElse super.receiveRecover

  def customReceiveCommand: Receive =
    Seq(
      processObligacionMessages
    ) reduce (_ orElse _)

  def customReceiveRecover: Receive = {
    case evt: ObjetoEvents =>
      state += evt
      evt match {
        case evt: ObjetoEvents.ObjetoUpdatedFromObligacion =>
          obligaciones((evt.sujetoId, evt.objetoId, evt.tipoObjeto, evt.obligacionId)) // waking up child
        case _ =>
      }
  }

  def processObligacionMessages: Receive = {
    case childMessage: ObligacionMessage =>
      val obligacion = obligaciones(
        (childMessage.sujetoId, childMessage.objetoId, childMessage.tipoObjeto, childMessage.obligacionId)
      )
      obligacion forward childMessage

      childMessage match {
        case obligacionUpdateFromDto: ObligacionUpdateFromDto =>
          state.exenciones.toSeq.foreach { exencion =>
            obligacion ! ObligacionCommands
              .ObligacionUpdateExencion(
                obligacionUpdateFromDto.deliveryId,
                obligacionUpdateFromDto.sujetoId,
                obligacionUpdateFromDto.objetoId,
                obligacionUpdateFromDto.tipoObjeto,
                obligacionUpdateFromDto.obligacionId,
                exencion
              )
          }
        case _ => ()
      }
  }

  import consumers.no_registral.objeto.infrastructure.json._

  def persistSnapshot(evt: ObjetoEvents, consolidatedState: ObjetoState)(handler: () => Unit): Unit = {
    val kafkaTopic = "ObjetoSnapshotPersistedReadside"
    val snapshot =
      ObjetoSnapshotPersisted(
        evt.deliveryId,
        evt.sujetoId,
        evt.objetoId,
        objetoId2 = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.objetoId2
          case _ => None
        },
        evt.tipoObjeto,
        consolidatedState.saldo,
        consolidatedState.sujetos,
        consolidatedState.tags,
        consolidatedState.sujetoResponsable,
        consolidatedState.porcentajeResponsabilidad,
        consolidatedState.registro,
        consolidatedState.obligacionesSaldo,
        consolidatedState.cuotas,
        operacion = ObligacionEvents.operaciones.get("Upsert").get,
        idExterno = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.idExterno
          case _ => None
        }
      )

    requirements.messageProducer.produce(
      data = Seq(
        KafkaKeyValue(
          snapshot.aggregateRoot,
          serialization.encode(snapshot)
        )
      ),
      topic = kafkaTopic
    ) { _ =>
      handler()
    }
  }
  def deleteSnapshot(evt: ObjetoEvents, consolidatedState: ObjetoState)(handler: () => Unit): Unit = {
    val kafkaTopic = "ObjetoSnapshotPersistedReadside"
    val snapshot =
      ObjetoSnapshotPersisted(
        evt.deliveryId,
        evt.sujetoId,
        evt.objetoId,
        objetoId2 = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.objetoId2
          case _ => None
        },
        evt.tipoObjeto,
        consolidatedState.saldo,
        consolidatedState.sujetos,
        consolidatedState.tags,
        consolidatedState.sujetoResponsable,
        consolidatedState.porcentajeResponsabilidad,
        consolidatedState.registro,
        consolidatedState.obligacionesSaldo,
        consolidatedState.cuotas,
        operacion = ObligacionEvents.operaciones.get("Delete").get,
        idExterno = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.idExterno
          case _ => None
        }
      )

    requirements.messageProducer.produce(
      data = Seq(
        KafkaKeyValue(
          snapshot.aggregateRoot,
          serialization.encode(snapshot)
        )
      ),
      topic = kafkaTopic
    ) { _ =>
      handler()
    }
  }
  def deleteObjetoObligacionesSnapshot(evt: ObjetoEvents, consolidatedState: ObjetoState)(handler: () => Unit): Unit = {
    val kafkaTopic = "ObjetoSnapshotPersistedReadside"
    val snapshot =
      ObjetoSnapshotPersisted(
        evt.deliveryId,
        evt.sujetoId,
        evt.objetoId,
        objetoId2 = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.objetoId2
          case _ => None
        },
        evt.tipoObjeto,
        consolidatedState.saldo,
        consolidatedState.sujetos,
        consolidatedState.tags,
        consolidatedState.sujetoResponsable,
        consolidatedState.porcentajeResponsabilidad,
        consolidatedState.registro,
        consolidatedState.obligacionesSaldo,
        consolidatedState.cuotas,
        operacion = ObligacionEvents.operaciones.get("FullDelete").get,
        idExterno = evt match {
          case evt: ObjetoEvents.ObjetoUpdatedFromObligacion => evt.idExterno
          case _ => None
        }
      )

    requirements.messageProducer.produce(
      data = Seq(
        KafkaKeyValue(
          snapshot.aggregateRoot,
          serialization.encode(snapshot)
        )
      ),
      topic = kafkaTopic
    ) { _ =>
      handler()
    }
  }

  /*def removeObligaciones(): Unit =
    state.obligaciones.foreach { obligacionId =>
      val aggregateRoots = ObjetoMessageRoots.extractor(persistenceId)
      val obligacion =
        obligaciones((aggregateRoots.sujetoId, aggregateRoots.objetoId, aggregateRoots.tipoObjeto, obligacionId))
      obligacion ! ObligacionCommands.ObligacionRemove(aggregateRoots.sujetoId,
                                                       aggregateRoots.objetoId,
                                                       aggregateRoots.tipoObjeto,
                                                       obligacionId)
    }

  def withCotitulares(sujetos: Set[String]): Boolean =
    sujetos.size > 1*/

  def informParent(cmd: ObjetoCommands, state: ObjetoState): Unit = {
    context.parent ! SujetoCommands.SujetoUpdateFromObjeto(
      cmd.deliveryId,
      cmd.sujetoId,
      cmd.objetoId,
      cmd.tipoObjeto,
      state.saldo,
      state.obligacionesSaldo.values.sum
    )
  }

  def informBajaToParent(cmd: ObjetoCommands): Unit = {
    context.parent ! SujetoCommands.SujetoSetBajaFromObjeto(
      cmd.deliveryId,
      cmd.sujetoId,
      cmd.objetoId,
      cmd.tipoObjeto
    )
  }

}

object ObjetoActor {
  def props(requirements: MonitoringAndMessageProducer): Props =
    Props(new ObjetoActor(requirements, None)).withDispatcher("my-dispatcher") //TODO added my-dispatcher
  type ObligacionAgregateRoot = (String, String, String, String)
  class ObjetoActorRefMap(newActor: ObligacionAgregateRoot => ActorRef)
      extends ActorRefMap[ObligacionAgregateRoot](newActor)
}
