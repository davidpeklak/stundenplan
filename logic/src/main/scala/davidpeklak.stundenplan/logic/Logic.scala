package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person

object Logic {

  var teacher: Option[Person] = None

  def setTeacher(person: Person): Unit = {
    teacher = Some(person)
  }

  def getTeacher: Option[Person] = {
    teacher
  }
}
