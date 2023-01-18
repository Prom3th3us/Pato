package cassandra

import com.datastax.oss.driver.api.core.CqlSession

object CqlSessionSingleton {
  val session: CqlSession = CqlSession.builder().build()
}
