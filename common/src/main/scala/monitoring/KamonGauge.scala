package monitoring

import kamon.Kamon

class KamonGauge(name: String) extends Gauge {

  private val gauge = Kamon
    .gauge("copernico-gauges")
    .withTag("entity", name)

  override def increment(): Unit = gauge.increment()
  override def decrement(): Unit = gauge.decrement()

  override def add(num: Int): Unit = gauge.increment(num)
  override def subtract(num: Int): Unit = gauge.decrement(num)

  override def set(num: Int): Unit = gauge.update(num)
}
