package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person
import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

class LogicSpec extends Specification {
  def is: SpecStructure = s2"""

The logic must
  remember who the teacher is       $setAndGetTeacher
    """

  def setAndGetTeacher: MatchResult[_] = {
     val teacher = new Person("The Teacher")

    Logic.setTeacher(teacher)

    Logic.getTeacher mustEqual  Some(teacher)
  }
}
