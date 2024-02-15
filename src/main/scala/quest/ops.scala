package quest

extension [T](in: T) {

  /**
   * Return the the success value or throw using EarlyExit. Must be used inside a quest block
   */
  inline def ?[S](using r: Resulting[T], support: QuestionOperatorSupport.Aux[T, S]): S = {
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
inline def bail[T: Resulting](value: T)(using support: QuestionOperatorSupport.Aux[T, _]): Nothing = {
  support.earlyExit(value)
}
