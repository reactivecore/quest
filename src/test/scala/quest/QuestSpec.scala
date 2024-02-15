package quest

class QuestSpec extends TestBase {

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

  it should "support either (success case with diverging error codes)" in {
    val a: Either[String, Int] = Right(23)
    val b: Either[String, Boolean] = Right(true)
    val res = quest {
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

  // Testing custom types

  sealed trait Base
  case class Err(msg: String) extends Base
  case class Ok(value: Int)   extends Base

  given support: QuestionOperatorSupport.Aux[Base, Err, Int] = new QuestionOperatorSupport[Base] {
    override type Failure = Err
    override type Success = Int

    override def decodeSuccess[X <: Base](result: X): Option[Int] = result match {
      case e: Err => None
      case ok: Ok => Some(ok.value)
    }
  }

  it should "support custom types" in {
    val x = quest {
      Ok(123).?
      Ok(235).?
      Ok(400)
    }
    x shouldBe Ok(400)

    val y = quest {
      Ok(123).?
      Err("boom").?
      Ok(400).?
    }
    y shouldBe Err("boom")
  }

  it should "handle correct return type" in {
    val x = quest {
      bail(Err("Boom!"))
      Ok(42)
    }


    x shouldBe Err("Boom!")
  }
}
