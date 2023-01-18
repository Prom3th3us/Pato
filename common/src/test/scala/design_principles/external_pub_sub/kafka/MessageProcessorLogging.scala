package design_principles.external_pub_sub.kafka

trait MessageProcessorLogging {
  var messageHistory: Seq[(String, String)] = Seq.empty
}
