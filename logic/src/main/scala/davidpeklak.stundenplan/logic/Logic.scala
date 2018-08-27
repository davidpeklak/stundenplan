package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person

class Logic(state: State) {

  import state._

  def setTeacher(person: Person): Unit = {
    teacher = Some(person)
  }

  def getTeacher: Option[Person] = {
    teacher
  }
}
