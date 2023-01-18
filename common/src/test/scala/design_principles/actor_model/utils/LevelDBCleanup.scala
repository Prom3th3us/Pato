package design_principles.actor_model.utils

import java.io.File

import akka.actor.ActorSystem
import org.apache.commons.io.FileUtils

object LevelDBCleanup {
  def cleanLevelDB()(implicit system: ActorSystem): Unit =
    try {
      val storageLocations =
        List(
          "akka.persistence.journal.leveldb.dir",
          "akka.persistence.journal.leveldb-shared.store.dir",
          "akka.persistence.snapshot-store.local.dir"
        ).map(s => new File(system.settings.config.getString(s)))
      storageLocations.foreach(FileUtils.deleteDirectory)
    } catch {
      case _: Throwable => ()
    }
}
