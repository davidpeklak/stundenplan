package davidpeklak.stundenplan.storage

import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.logic.State

trait Storage {
  def createPerson(name: String): Person

  def setTeacher(personId: PersonId): Unit

  def load: State
}
