package davidpeklak.stundenplan.storage

import davidpeklak.stundenplan.person.Person
import davidpeklak.stundenplan.logic.State

trait Storage {
  def storeTeacher(person: Person): Unit

  def load: State
}
