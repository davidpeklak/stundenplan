package davidpeklak.stundenplan.storage

import java.io.File

import davidpeklak.stundenplan.person.Person
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
    val teacher = Person("Maria Presch")

    {
      val fileStorage1 = new FileStorage(baseDir)
      fileStorage1.storeTeacher(teacher)
    }

    {
      val fileStorage2 = new FileStorage(baseDir)
      val state = fileStorage2.load
      state.teacher mustEqual Some(teacher)
    }
  }
}
