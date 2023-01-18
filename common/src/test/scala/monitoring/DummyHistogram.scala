package monitoring

class DummyHistogram(name: String) extends Histogram {
  override def record(value: Long): Unit = ()
}
