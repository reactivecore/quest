package quest

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class TestBase extends AnyFlatSpec with Matchers with BeforeAndAfterEach {}
