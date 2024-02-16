package quest

import scala.annotation.implicitNotFound

/** Helper trait for implementing the Question-Operator syntax for different types. */
@implicitNotFound("Could not find QuestionOperatorSupport for ${T}")
trait QuestionOperatorSupport[-T] {

  /** Success type. */
  type Success

  /** Failure Type. */
  type Failure

  /** Split some result into success or failure. */
  def decode[X <: T](value: X): Either[Failure, Success]
}

object QuestionOperatorSupport {
  type Aux[T, F, S] = QuestionOperatorSupport[T] {
    type Failure = F
    type Success = S
  }

  given forEither[L, R]: QuestionOperatorSupport.Aux[Either[L, R], Left[L, Nothing], R] =
    new QuestionOperatorSupport[Either[L, R]] {
      override type Failure = Left[L,Nothing]
      override type Success = R

      override def decode[X <: Either[L, R]](value: X): Either[Left[L, Nothing], R] = {
        value match {
          case Left(l) => Left(Left(l))
          case Right(r) =>  Right(r)
        }
      }
    }

  given forOption[T]: QuestionOperatorSupport.Aux[Option[T], None.type, T] = {
    new QuestionOperatorSupport[Option[T]] {
      override type Failure = None.type
      override type Success = T

      override def decode[X <: Option[T]](value: X): Either[None.type, T] = {
        value.toRight(None)
      }
    }
  }
}
