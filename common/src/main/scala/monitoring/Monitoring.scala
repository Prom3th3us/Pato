package monitoring

trait Monitoring {
  def counter(name: String, context: Map[String, String] = Map.empty): Counter

  def histogram(name: String): Histogram

  def gauge(name: String): Gauge
}
