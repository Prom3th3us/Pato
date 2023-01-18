package monitoring

import kamon.Kamon

class KamonHistogram(name: String) extends Histogram {

  private val histogram = Kamon
    .histogram("copernico-histograms")
    .withTag("entity", name)

  override def record(value: Long): Unit = histogram.record(value)
}
