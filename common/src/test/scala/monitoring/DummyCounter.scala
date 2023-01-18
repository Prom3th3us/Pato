package monitoring

class DummyCounter(name: String) extends Counter {
  override def increment(): Unit = ()
  override def add(num: Int): Unit = ()
}
