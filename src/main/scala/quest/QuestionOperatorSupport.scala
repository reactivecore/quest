package quest

import scala.annotation.implicitNotFound

/** Helper trait for implementing the Question-Operator syntax for different types. */
@implicitNotFound("Could not find QuestionOperatorSupport for ${T}")
trait QuestionOperatorSupport[-T] {

  /** Success type. */
  type Success

  /** Split some result into sucess or failure. */
  def decodeSuccess[X <: T](result: X): Option[Success]

  /** Run f and returns successful values or throws EarlyExit with full value */
  @throws[EarlyExit[_]]
  inline def getOrEarlyExit[X <: T](value: X): Success = {
    decodeSuccess(value).getOrElse {
      earlyExit(value)
    }
  }

  /** Issue early exit. */
  inline def earlyExit[X <: T](value: X): Nothing = {
    throw EarlyExit(value)
  }
}

object QuestionOperatorSupport {
  type Aux[T, S] = QuestionOperatorSupport[T] {
    type Success = S
  }

  given forEither[L, R]: QuestionOperatorSupport.Aux[Either[L, R], R] =
    new QuestionOperatorSupport[Either[L, R]] {
      override type Success = R

      override def decodeSuccess[X <: Either[L, R]](result: X): Option[R] = {
        result.toOption
      }
    }

  given forOption[T]: QuestionOperatorSupport.Aux[Option[T], T] = {
    new QuestionOperatorSupport[Option[T]] {
      override type Success = T

      override def decodeSuccess[X <: Option[T]](result: X): Option[T] = {
        result
      }
    }
  }
}
