package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.{Person, PersonId}

import scala.collection.mutable

class State {

  var persons: mutable.Map[PersonId, Person] = mutable.Map()

  var students: mutable.Set[PersonId] = mutable.Set()

  var teacherOpt: Option[PersonId] = None
}
