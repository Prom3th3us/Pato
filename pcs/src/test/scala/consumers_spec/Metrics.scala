package consumers_spec

object Metrics {

  def totalEntityCount(actor: akka.actor.typed.ActorRef[_]): Int = {
    import akka.actor.typed.scaladsl.adapter._
    totalEntityCount(actor.toClassic)
  }

  def totalEntityCount(actor: akka.actor.ActorRef): Int = {
    import akka.pattern.ask
    import akka.util.Timeout
    import org.scalatest.concurrent.ScalaFutures._

    import scala.concurrent.duration._

    implicit val timeout: Timeout = Timeout(6.seconds)
    val statsBefore =
      (actor ? akka.cluster.sharding.ShardRegion.GetClusterShardingStats(5.seconds))
        .mapTo[akka.cluster.sharding.ShardRegion.ClusterShardingStats]
    val totalCount = statsBefore.futureValue.regions.values.flatMap(_.stats.values).sum
    totalCount
  }
}
