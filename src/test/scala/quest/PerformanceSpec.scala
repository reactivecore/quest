package quest

class PerformanceSpec extends TestBase {

  def sucMod(in: Int, mod: Int): Option[Int] = {
    val cand = in + 1
    if (cand >= mod) {
      None
    } else {
      Some(cand)
    }
  }

  /*
  Test method: Three different methods which increment a number by three, but not if it leaves it's modulo range.
  The function is called with a lot of numbers and smaller mod, so it ensures that a lot of error returns are created.
  It should measure the overhead of the exception throwing inside quest.
   */

  def withFlatMap(in: Int, mod: Int): Option[Int] = {
    for {
      a <- sucMod(in, mod)
      b <- sucMod(a, mod)
      c <- sucMod(b, mod)
    } yield {
      c
    }
  }

  def withReturnAndPatternMatch(in: Int, mod: Int): Option[Int] = {
    val a = sucMod(in, mod) match {
      case None    => return None
      case Some(a) => a
    }
    val b = sucMod(a, mod) match {
      case None    => return None
      case Some(b) => b
    }
    sucMod(b, mod)
  }

  def withQuest(in: Int, mod: Int): Option[Int] = quest {
    val a = sucMod(in, mod).?
    val b = sucMod(a, mod).?
    sucMod(b, mod)
  }

  inline def testIterations(size: Int, mod: Int, inline f: (Int, Int) => Option[Int], message: String): Unit = {
    val it = (0 until size).iterator
    val t0 = System.nanoTime()
    while (it.hasNext) {
      val i      = it.next()
      val result = f(i, mod)
      if (i < mod - 3) {
        result shouldBe Some(i + 3)
      } else {
        result shouldBe None
      }
    }
    val t1 = System.nanoTime()
    println(
      s"${message}, iterations=${size}, mod=${mod} dt=${(t1 - t0).toDouble / 1_000_000_000}s, per call=${(t1 - t0).toDouble / size}ns"
    )
  }

  def testAll(size: Int, mod: Int): Unit = {
    s"size ${size}" should "work" in {
      for (round <- 0 until 10) {
        println(s"Round: ${round}")
        testIterations(size, mod, withFlatMap, "withFlatMap")
        testIterations(size, mod, withReturnAndPatternMatch, "withReturnAndPatternMatch")
        testIterations(size, mod, withQuest, "withQuest")
      }
    }
  }

  testAll(10, 3)
  testAll(1000, 300)
  testAll(100000, 30000)
  testAll(10000000, 3000000) // More than 7 Million exceptions
}
