package davidpeklak.stundenplan.storage

import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.logic.State

trait Storage {
  def createPerson(name: String): Person

  def updatePerson(person: Person): Unit

  def deletePerson(id: PersonId): Unit

  def addStudent(id: PersonId): Unit

  def removeStudent(id: PersonId): Unit

  def setTeacher(personId: PersonId): Unit

  def load: State
}
