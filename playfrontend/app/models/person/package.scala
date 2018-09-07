package models

import davidpeklak.stundenplan.person.{Person, PersonId}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{parse, compact, render}

package object person {

  implicit class JsPerson(p: Person) {
    def toJson: String = {
      compact(render(("id" -> p.id.id) ~ ("name" -> p.name)))
    }
  }

  def personFromJson(str: String): Person = {
    case class JsPerson(id: Int, name: String)
    val jObject = parse(str)
    implicit val formats = DefaultFormats
    val jsPerson = jObject.extract[JsPerson]
    Person(PersonId(jsPerson.id), jsPerson.name)
  }

}
