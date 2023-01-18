package infrastructure.kafka.zk

case class AdminZkClient(zkClient: KafkaZkClient) {
  def createTopic(topic: String, i: Int, i1: Int): Unit = ???

  def deleteTopic(topic: String): Unit = ???

}
