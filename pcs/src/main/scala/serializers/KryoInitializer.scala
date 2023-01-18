package serializers

import akka.actor.ExtendedActorSystem
import io.altoo.akka.serialization.kryo.DefaultKryoInitializer
import io.altoo.akka.serialization.kryo.serializer.scala.ScalaKryo

class KryoInitializer extends DefaultKryoInitializer {
  override def preInit(kryo: ScalaKryo, system: ExtendedActorSystem): Unit = {
    kryo.setDefaultSerializer(classOf[com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer[_]])
    preInit(kryo)
  }
}
