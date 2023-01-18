package infrastructure.kafka.zk

case class KafkaZkClient(config: String,
                         isSecure: Boolean,
                         sessionTimeoutMs: Long,
                         connectionTimeoutMs: Long,
                         maxInFlightRequests: Int,
                         time: Any) {
  //todo code me!
}
