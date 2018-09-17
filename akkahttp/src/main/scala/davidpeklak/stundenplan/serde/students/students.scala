package davidpeklak.stundenplan.serde

import davidpeklak.stundenplan.person.Person
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{compact, render}

package object students {

  implicit class JsStudents(students: Iterable[Person]) {
    def toJson: String = {
      compact(render(
        students
          .toList
          .map(p => ("id" -> p.id.id) ~ ("name" -> p.name))
      ))
    }
  }

}