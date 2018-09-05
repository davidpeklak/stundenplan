package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.{Person, PersonId}

class State {

  var persons: Map[PersonId, Person] = Map()

  var teacherOpt: Option[PersonId] = None
}
