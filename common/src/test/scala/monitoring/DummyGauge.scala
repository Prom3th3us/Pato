package monitoring

class DummyGauge(name: String) extends Gauge {
  override def increment(): Unit = ()
  override def decrement(): Unit = ()

  override def add(num: Int): Unit = ()
  override def subtract(num: Int): Unit = ()

  override def set(num: Int): Unit = ()
}
