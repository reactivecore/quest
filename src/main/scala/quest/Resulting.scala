package quest

import scala.annotation.implicitNotFound

/**
 * Helper trait encoding the result of a function in a context function.
 */
@implicitNotFound("No resulting Resulting[\\${R}] was found, are you inside a quest-Block?")
trait Resulting[-R] {}

object Resulting {
  inline def instance[R]: Resulting[R] = new Resulting[R] {}
}
