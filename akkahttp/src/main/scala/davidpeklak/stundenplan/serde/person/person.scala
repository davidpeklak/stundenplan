package davidpeklak.stundenplan.serde

import java.io.InputStream

import davidpeklak.stundenplan.person.{Person, PersonId}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{compact, parse, render}

package object person {

  implicit class JsPerson(p: Person) {
    def toJson: String = {
      compact(render(("id" -> p.id.id) ~ ("name" -> p.name)))
    }
  }

  def personFromJson(jValue: JValue): Person = {
    case class JsPerson(id: Int, name: String)

    implicit val formats = DefaultFormats
    val jsPerson = jValue.extract[JsPerson]
    Person(PersonId(jsPerson.id), jsPerson.name)
  }

  def personFromJsonString(str: String): Person = {
    val jValue = parse(str)
    personFromJson(jValue)
  }

  def personFromJasonInputStream(is: InputStream): Person = {
    val jValue = parse(is)
    personFromJson(jValue)
  }

}
