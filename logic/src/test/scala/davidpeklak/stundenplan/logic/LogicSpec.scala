package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person
import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

class LogicSpec extends Specification {
  def is: SpecStructure =
    s2"""

The logic must
  remember who the teacher is       $setAndGetTeacher
  must store the teacher
    """

  private def newLogic: Logic = {
    val state = new State
    new Logic(state)
  }

  def setAndGetTeacher: MatchResult[_] = {
    val teacher = Person("The Teacher")

    val logic = newLogic

    logic.setTeacher(teacher)

    logic.getTeacher mustEqual Some(teacher)
  }
}
