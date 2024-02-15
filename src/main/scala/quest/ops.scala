package quest

extension [T](in: T) {

  /**
   * Return the the success value or return the quest-block with the error value.
   * Must be used inside a quest block
   */
  inline def ?[F, S](using support: QuestionOperatorSupport.Aux[T, F, S], resulting: Resulting[F]): S = {
    support.getOrEarlyExit(in)
  }
}

/** Start a quest block. Inside the block, the question operator can be used. */
inline def quest[T](f: Resulting[T] ?=> T): T = {
  given r: Resulting[T] = Resulting.instance

  try {
    f
  } catch {
    case EarlyExit(value: T @unchecked) =>
      value
  }
}

/**
 * Immediately return the [[quest]] method returning value.
 * You can also use `return`, but this doesn't always works in closures.
 * */
inline def bail[T: Resulting](value: T)(using support: QuestionOperatorSupport.Aux[T, _, _]): Nothing = {
  support.earlyExit(value)
}
