package davidpeklak.stundenplan.logic

import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.storage.Storage
import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure
import org.specs2.mock.Mockito

class LogicSpec extends Specification with Mockito {
  def is: SpecStructure =
    s2"""

The logic must
  call the storage to create a Person                                ${new LogicFixture().callStorageToCreatePerson}
  throw an exception if the storage creates the same PersonId twice  ${new LogicFixture().throwIfTheStorageReturnsTheSameIdTwice}
  remember a person that has been created                            ${new LogicFixture().rememberPerson}
  remember the teacher                                               ${new LogicFixture().rememberTeacher}
  call the storage to set the teacher                                ${new LogicFixture().callStorageToSetTeacher}
    """

  class LogicFixture {
    val state = new State

    val storageMock = mock[Storage]

    val logic = new Logic(state, storageMock)

    def callStorageToCreatePerson: MatchResult[_] = {
      val name = "Karli"

      storageMock.createPerson(name) returns Person(PersonId(1), name)

      logic.createPerson(name)

      there was one(storageMock).createPerson(name)
    }

    def throwIfTheStorageReturnsTheSameIdTwice: MatchResult[_] = {
      val name = "Karli"
      val alwaysTheSamePersonId = PersonId(1)

      storageMock.createPerson(name) returns Person(alwaysTheSamePersonId, name)

      logic.createPerson(name)
      logic.createPerson(name) must throwA[IllegalStateException]
    }

    def rememberPerson: MatchResult[_] = {
      val name = "Karli"

      storageMock.createPerson(name) returns Person(PersonId(1), name)

      val person = logic.createPerson(name)

      val rememberedPerson = logic.getPerson(person.id)

      rememberedPerson must beSome(person)
    }

    def rememberTeacher: MatchResult[_] = {
      val name = "Teacher"

      storageMock.createPerson(name) returns Person(PersonId(1), name)

      val teacher = logic.createPerson(name)

      logic.setTeacher(teacher.id)

      logic.getTeacher must beSome(teacher)
    }

    def callStorageToSetTeacher: MatchResult[_] = {
      val name = "Teacher"

      storageMock.createPerson(name) returns Person(PersonId(1), name)

      val teacher = logic.createPerson(name)

      logic.setTeacher(teacher.id)

      there was one(storageMock).setTeacher(teacher.id)
    }
  }

}
