package quest

class EarlyExitSpec extends TestBase {

  it should "work in a simple case" in {
    val a: Option[Int] = Some(3)
    val b: Option[Int] = None

    quest {
      Some(a.? + b.?)
    } shouldBe None
  }

  it should "support either" in {
    val a: Either[String, Int] = Right(23)
    val b: Either[String, Boolean] = Left("Bad")

    quest {
      val x = a.?
      val y = b.?
    } shouldBe Left("Bad")
  }

  it should "support either (success case)" in {
    val a: Either[String, Int] = Right(23)
    val b: Either[String, Boolean] = Right(true)
    val res: Either[String, Int] = quest {
      val x = a.?
      val y = b.?
      val z = if (y) x else -1
      Right(z)
    }
    res shouldBe Right(23)
  }

  it should "support bailing" in {
    val x: Option[Int] = quest {
      bail(None)
      Some(5)
    }
    x shouldBe None

    val y: Option[Int] = quest {
      bail(Some(3))
      None
    }
    y shouldBe Some(3)
  }

  sealed trait Base
  case class Err(msg: String) extends Base
  case class Ok(value: Int)   extends Base

  given support: QuestionOperatorSupport.Aux[Base, Ok] = new QuestionOperatorSupport[Base] {
    override type Success = Ok

    override def decodeSuccess[X <: Base](result: X): Option[Ok] = result match {
      case e: Err => None
      case ok: Ok => Some(ok)
    }
  }

  it should "handle correct return type" in {
    val x = quest {
      bail(Err("Boom!"))
      Ok(42)
    }

    x shouldBe Err("Boom!")
  }
}
