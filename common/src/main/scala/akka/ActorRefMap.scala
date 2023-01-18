package akka

import akka.actor.ActorRef

abstract class ActorRefMap[Key](newActor: Key => ActorRef) extends collection.immutable.Map[Key, ActorRef] {
  protected var map = collection.immutable.Map.empty[Key, ActorRef] // TODO why var

  override def apply(key: Key): ActorRef = get(key) match {
    case None =>
      this.iterator
      val ref = newActor(key)
      map = this.+((key, ref))
      ref
    case Some(ref) => ref
  }

  def get(key: Key): Option[ActorRef] = {
    map.get(key)
  }

  override def removed(key: Key): scala.collection.immutable.Map[Key, akka.actor.ActorRef] = {
    map - key
  }
  override def updated[V1 >: akka.actor.ActorRef](key: Key, value: V1): scala.collection.immutable.Map[Key, V1] = {
    map.+((key, value)) // (key,value)
  }
  def iterator: Iterator[(Key, ActorRef)] =
    map.iterator

}
