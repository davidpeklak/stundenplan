package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.storage.Storage

class Logic(state: State,
            storage: Storage) {

  import state._

  def createPerson(name: String): Person = {
    val newPerson = storage.createPerson(name)
    if (persons.contains(newPerson.id)) {
      throw new IllegalStateException(s"storage created duplicate ${newPerson.id}")
    }
    else {
      persons += (newPerson.id -> newPerson)
      newPerson
    }
  }

  def getPerson(id: PersonId): Option[Person] = {
    persons.get(id)
  }

  def setTeacher(personId: PersonId): Unit = {
    if (persons.contains(personId)) {
      storage.setTeacher(personId)
      teacherOpt = Some(personId)
    }
    else {
      throw new IllegalArgumentException(s"A person with $personId does not exist")
    }
  }

  def getTeacher: Option[Person] = {
    for {
      id <- teacherOpt
      person <- persons.get(id)
    } yield person
  }
}
