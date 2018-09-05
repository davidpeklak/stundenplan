package davidpeklak.stundenplan.storage

import java.io.File

import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

class FileStorageSpec extends Specification {
  def is: SpecStructure = s2"""

A FileStorage that must be able to load state
that another FileStorage has written before      $e1

    """

  def e1: MatchResult[_] = {
    val baseDir = new File("target")
    val name = "Maria Presch"

    {
      val fileStorage1 = new FileStorage(baseDir)
      val person = fileStorage1.createPerson(name)
      fileStorage1.setTeacher(person.id)
    }

    {
      val fileStorage2 = new FileStorage(baseDir)
      val state = fileStorage2.load
      val teacher = for {
        teacherId <- state.teacherOpt
        teacher <- state.persons.get(teacherId)
      } yield teacher

      teacher.map(_.name) must beSome(name)
    }
  }
}
