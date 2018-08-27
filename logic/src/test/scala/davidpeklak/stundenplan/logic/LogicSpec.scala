package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.Person
import davidpeklak.stundenplan.storage.Storage
import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure
import org.specs2.mock.Mockito

class LogicSpec extends Specification with Mockito {
  def is: SpecStructure =
    s2"""

The logic must
  remember who the teacher is       ${new LogicFixture().setAndGetTeacher}
  must store the teacher            ${new LogicFixture().storeTeacher}
    """

  class LogicFixture {
    val state = new State

    val storageMock = mock[Storage]

    val logic = new Logic(state, storageMock)


    def setAndGetTeacher: MatchResult[_] = {
      val teacher = Person("The Teacher")

      logic.setTeacher(teacher)

      logic.getTeacher mustEqual Some(teacher)
    }

    def storeTeacher: MatchResult[_] = {
      val teacher = Person("The Teacher")

      logic.setTeacher(teacher)

      there was one(storageMock).storeTeacher(teacher)
    }
  }

}
