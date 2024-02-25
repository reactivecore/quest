package quest

import scala.util.boundary.{Label, break}
import scala.util.boundary

/**
 * Start a quest block. Inside the block, the question operator can be used.
 *
 * Note: this wraps [[scala.util.boundary.apply]].
 */
inline def quest[T](f: Label[T] ?=> T): T = {
  boundary {
    f
  }
}

extension [T](in: T) {

  /**
   * Return the success value or return the quest-block with the error value.
   *
   * Must be used inside a quest block
   */
  inline def ?[F, S](using support: QuestionOperatorSupport.Aux[T, F, S], label: Label[F]): S = {
    support.decode(in) match {
      case Left(bad: F) => break(bad)
      case Right(ok)    => ok
    }
  }
}

/**
 * Immediately return the [[quest]] method returning value.
 *
 * You can also use `return`, but this doesn't always works in closures.
 *
 * Must be used inside a quest block
 *
 * Note: this wraps [[scala.util.boundary.break]]
 */
inline def bail[T: Label](value: T): Nothing = {
  break(value)
}
