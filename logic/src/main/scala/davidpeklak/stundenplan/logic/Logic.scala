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

  def updatePerson(newPerson: Person): Unit = {
    if (!persons.contains(newPerson.id)) {
      throw new IllegalArgumentException(s"cannot update person with id ${newPerson.id}. id not found")
    }
    else {
      storage.updatePerson(newPerson)
      persons(newPerson.id) = newPerson
    }
  }

  def deletePerson(id: PersonId): Unit = {
    if (!persons.contains(id)) {
      throw new IllegalArgumentException(s"failed to delete person with id $id. It does not exists.")
    }
    if (teacherOpt.contains(id)) {
      throw new IllegalArgumentException(s"cannot delete person with id $id. It is set as teacher.")
    }
    storage.deletePerson(id)
    persons.remove(id)
  }

  def getPerson(id: PersonId): Option[Person] = {
    persons.get(id)
  }

  def addStudent(id: PersonId): Unit = {
    if (!persons.contains(id)) {
      throw new IllegalArgumentException(s"failed to set person with id $id as a student. It does not exists.")
    }
    if (teacherOpt.contains(id)) {
      throw new IllegalArgumentException(s"failed to set person with id $id as a student. It is the teacher")
    }
    storage.addStudent(id)
    students += id
  }

  def getStudents: Set[Person] = {
    students.map(persons).toSet
  }

  def removeStudent(id: PersonId): Unit = {
    if (!persons.contains(id)) {
      throw new IllegalArgumentException(s"failed to set person with id $id as a student. It does not exists.")
    }
    storage.removeStudent(id)
    students -= id
  }

  def setTeacher(id: PersonId): Unit = {
    if (!persons.contains(id)) {
      throw new IllegalArgumentException(s"failed to set person with id $id as teacher. It does not exists.")
    }
    if (students.contains(id)) {
      throw new IllegalArgumentException(s"failed to set person with id $id as teacher. It is a student")
    }

    storage.setTeacher(id)
    teacherOpt = Some(id)
  }

  def getTeacher: Option[Person] = {
    for {
      id <- teacherOpt
      person <- persons.get(id)
    } yield person
  }
}
