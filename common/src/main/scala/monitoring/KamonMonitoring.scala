package monitoring

import kamon.Kamon

class KamonMonitoring extends Monitoring {

  // init Kamon
  Kamon.init()

  override def counter(name: String, context: Map[String, String] = Map.empty): Counter =
    new KamonCounter(name, context)

  override def histogram(name: String): Histogram = new KamonHistogram(name)

  override def gauge(name: String): Gauge = new KamonGauge(name)
}
