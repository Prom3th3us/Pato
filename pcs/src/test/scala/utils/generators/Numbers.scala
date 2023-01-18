package utils.generators

import java.util.concurrent.atomic.AtomicInteger

object Numbers {
  private val nextIntC = new AtomicInteger()
  def nextInt = nextIntC.incrementAndGet()

  private val nextIntFromC = new AtomicInteger()
  def nextIntFrom(startingPoint: Int) = startingPoint + nextIntFromC.incrementAndGet()

  def positiveNumber = math.abs(nextInt)

  private val kafkaPortsC = new AtomicInteger()
  def nextKafkaPort = {
    val minimalKafkaPort = 9000
    minimalKafkaPort + kafkaPortsC.incrementAndGet()
  }
}
