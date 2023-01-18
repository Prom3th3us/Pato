package monitoring

import kamon.Kamon
import kamon.tag.TagSet

class KamonCounter(name: String, context: Map[String, String]) extends Counter {
  private val tags = TagSet.from(context + ("entity" -> name))

  private val counter = Kamon
    .counter("copernico-counters")
    .withTags(tags)

  override def increment(): Unit = counter.increment()
  override def add(num: Int): Unit = counter.increment(num)
}
