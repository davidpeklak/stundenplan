package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person
import davidpeklak.stundenplan.storage.Storage

class Logic(state: State,
            storage: Storage) {

  import state._

  def setTeacher(person: Person): Unit = {
    teacher = Some(person)
    storage.storeTeacher(person)
  }

  def getTeacher: Option[Person] = {
    teacher
  }
}
