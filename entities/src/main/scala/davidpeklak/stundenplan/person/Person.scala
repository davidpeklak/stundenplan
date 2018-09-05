package davidpeklak.stundenplan.person

case class PersonId(id: Int) extends AnyVal

case class Person(id: PersonId, name: String)
