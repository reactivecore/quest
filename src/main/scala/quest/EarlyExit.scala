package quest

/** Exception used for early exit. Catched by [[quest]] */
case class EarlyExit[T](value: T) extends Throwable(null, null, false, false)
