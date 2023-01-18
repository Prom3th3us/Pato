package akka.dispatchers

trait Dispatchers {

  def fixedThreadPool(dispatcherName: String, fixedPoolSize: Int, throughput: Int): String =
    s"""$dispatcherName {
    |  type = "Dispatcher"
    |  executor = "thread-pool-executor"
    |
    |  thread-pool-executor {
    |    fixed-pool-size = $fixedPoolSize
    |  }
    |
    |  throughput = $throughput
    |}""".stripMargin

  def forkJoin(dispatcherName: String,
               parallelismMin: Int,
               parallelismMax: Int,
               parallelismFactor: Double,
               throughput: Int): String =
    s"""
      |$dispatcherName {
      |  type = "Dispatcher"
      |  executor = "fork-join-executor"
      |
      |  fork-join-executor {
      |    parallelism-min = $parallelismMin
      |    parallelism-max = $parallelismMax
      |    parallelism-factor = $parallelismFactor
      |  }
      |
      |  throughput = $throughput
      |}""".stripMargin
}
