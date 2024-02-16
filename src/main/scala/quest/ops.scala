package quest

import scala.util.boundary.{Label, break}
import scala.util.boundary

extension [T](in: T) {

  /**
   * Return the the success value or return the quest-block with the error value.
   * Must be used inside a quest block
   */
  inline def ?[F, S](using support: QuestionOperatorSupport.Aux[T, F, S], label: Label[F]): S = {
    support.decode(in) match {
      case Left(bad) => break(bad)
      case Right(ok) => ok
    }
  }
}

/** Start a quest block. Inside the block, the question operator can be used. */
inline def quest[T](f: Label[T] ?=> T): T = {
  boundary {
    f
  }
}

/**
 * Immediately return the [[quest]] method returning value.
 * You can also use `return`, but this doesn't always works in closures.
 * */
inline def bail[T: Label](value: T): Nothing = {
  break(value)
}
