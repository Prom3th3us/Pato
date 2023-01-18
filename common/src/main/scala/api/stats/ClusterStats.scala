package api.stats

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.http.scaladsl.server.Route
import akka.management.cluster.scaladsl.ClusterHttpManagementRoutes

class ClusterStats(implicit ctx: ActorSystem) {

  val cluster: Cluster = Cluster(ctx)
  val route: Route = ClusterHttpManagementRoutes(cluster)
}
