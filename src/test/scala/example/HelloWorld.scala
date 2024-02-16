package example
import quest.*

def getUser(id: Int): Option[String] = {
  id match {
    case 10 => Some("Alice")
    case 11 => Some("Bob")
    case _  => None
  }
}

def getPermissions(id: Int): Option[List[String]] = {
  id match {
    case 10 => Some(List("Admin"))
    case _  => None
  }
}

def getUserAndPermissions(id: Int): Option[(String, List[String])] = quest {
  val user        = getUser(id).?
  val permissions = getPermissions(id).?
  Some((user, permissions))
}

object HelloWorld extends App {

  println(getUserAndPermissions(10))
  println(getUserAndPermissions(11))
  println(getUserAndPermissions(12))

}
