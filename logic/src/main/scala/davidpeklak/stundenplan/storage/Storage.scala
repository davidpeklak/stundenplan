package davidpeklak.stundenplan.storage

import davidpeklak.stundenplan.person.Person

trait Storage {
  def storeTeacher(person: Person): Unit
}
