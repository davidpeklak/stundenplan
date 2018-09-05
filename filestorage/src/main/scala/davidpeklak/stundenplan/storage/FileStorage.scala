package davidpeklak.stundenplan.storage

import java.io.{File, PrintWriter}

import davidpeklak.stundenplan.logic.State
import davidpeklak.stundenplan.person.{Person, PersonId}

import scala.io.{Codec, Source}

class FileStorage(baseDir: File) extends Storage {

  import FileUtil._

  private lazy val personDir: File = tryMkDirs(baseDir / "persons")
  private lazy val teacherFile = baseDir / "teacher"


  def createPerson(name: String): Person = {
    val existingIds = personDir.listFiles.toSeq
      .filter(_.isFile)
      .map(_.getName)
      .filter(_.startsWith("person"))
      .map(_.drop("person".length))
      .map(Integer.parseUnsignedInt)
      .map(PersonId)
      .toSet

    val firstAvailableId = Stream.from(0)
      .map(PersonId)
      .filterNot(existingIds)
      .head

    val file = personDir / s"person${firstAvailableId.id}"

    file.createNewFile()
    val pw = new PrintWriter(file, "UTF-8")
    pw.write(name)
    pw.close()

    Person(firstAvailableId, name)
  }

  def setTeacher(personId: PersonId): Unit = {
    teacherFile.delete()
    teacherFile.createNewFile()
    val pw = new PrintWriter(teacherFile, "UTF-8")
    pw.write(personId.id.toString)
    pw.close()
  }

  def load: State = {
    val state = new State()
    state.persons = loadPersons()
    state.teacherOpt = loadTeacherIdOpt()
    state
  }

  private def loadPersons(): Map[PersonId, Person] = {
    def extractPerson(file: File): Person = {
      val id = PersonId(Integer.parseInt(file.getName.drop("person".length)))
      val name = Source.fromFile(file)(Codec.UTF8).mkString
      Person(id, name)
    }

    if(!personDir.isDirectory) {
      Map.empty
    } else {
      personDir.listFiles.toSeq
        .filter(_.isFile)
        .filter(_.getName.startsWith("person"))
        .map(extractPerson)
        .map(p => p.id -> p)
        .toMap
    }
  }


  private def loadTeacherIdOpt(): Option[PersonId] = {
    if (!teacherFile.canRead) {
      None
    } else {
      val string = Source.fromFile(teacherFile)(Codec.UTF8).mkString
      if (string.isEmpty) {
        None
      } else {
        Some(PersonId(Integer.parseInt(string)))
      }
    }
  }
}
