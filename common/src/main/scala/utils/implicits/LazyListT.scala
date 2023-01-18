package utils.implicits

object LazyListT {
  implicit class ImprovedLazyList[A <: Comparable[A]](stream1: LazyList[A]) {

    def merge(stream2: LazyList[A])(implicit nParallelProcesses: Int): LazyList[A] = merge(stream1, stream2)
    private def merge(stream1: LazyList[A], stream2: LazyList[A])(implicit nParallelProcesses: Int): LazyList[A] =
      (stream1.isEmpty, stream2.isEmpty) match {
        case (false, false) =>
          val diminishingProportion = Math.min(0.98, 0.9 + (nParallelProcesses.toDouble / 1000))
          if (math.random < diminishingProportion) {
            stream1.head #:: merge(stream1.tail, stream2)
          } else {
            stream2.head #:: merge(stream1, stream2.tail)
          }
        case (false, true) => stream1
        case (true, false) => stream2
        case (true, true) => LazyList.empty[A]
      }
  }

}
