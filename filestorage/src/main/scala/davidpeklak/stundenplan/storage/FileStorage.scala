package davidpeklak.stundenplan.storage

import java.io.{File, PrintWriter}

import davidpeklak.stundenplan.logic.State
import davidpeklak.stundenplan.person.{Person, PersonId}

import scala.collection.mutable
import scala.io.{Codec, Source}

class FileStorage(baseDir: File) extends Storage {

  import FileUtil._

  private lazy val personDir: File = tryMkDirs(baseDir / "persons")
  private lazy val studentDir: File = tryMkDirs(baseDir / "students")
  private lazy val teacherFile = baseDir / "teacher"

  private def personFileForId(id: PersonId): File = {
    personDir / s"person${id.id}"
  }

  private def studentFileForId(id: PersonId): File = {
    studentDir / s"id"
  }

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

    val file = personFileForId(firstAvailableId)

    file.createNewFile()
    val pw = new PrintWriter(file, "UTF-8")
    pw.write(name)
    pw.close()

    Person(firstAvailableId, name)
  }

  def updatePerson(person: Person): Unit = {
    val file = personFileForId(person.id)
    if (!file.exists()) {
      throw new IllegalStateException(s"Failed to update person with id ${person.id}: File not found")
    }
    else {
      file.delete()
      file.createNewFile()
      val pw = new PrintWriter(file, "UTF-8")
      pw.write(person.name)
      pw.close()
    }
  }

  def deletePerson(id: PersonId): Unit = {
    val file = personFileForId(id)
    if (!file.exists()) {
      throw new IllegalStateException(s"Failed to delete person with id $id: File not found")
    }
    file.delete()
  }

  def addStudent(id: PersonId): Unit = {
    val file = studentFileForId(id)
    file.createNewFile()
  }

  def removeStudent(id: PersonId): Unit = {
    val file = studentFileForId(id)
    file.delete()
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
    state.students = loadStudents()
    state.teacherOpt = loadTeacherIdOpt()
    state
  }

  private def loadPersons(): mutable.Map[PersonId, Person] = {
    def extractPerson(file: File): Person = {
      val id = PersonId(Integer.parseInt(file.getName.drop("person".length)))
      val name = Source.fromFile(file)(Codec.UTF8).mkString
      Person(id, name)
    }

    if (!personDir.isDirectory) {
      mutable.Map.empty
    } else {
      mutable.Map.empty ++ personDir.listFiles.toSeq
        .filter(_.isFile)
        .filter(_.getName.startsWith("person"))
        .map(extractPerson)
        .map(p => p.id -> p)
    }
  }

  private def loadStudents(): mutable.Set[PersonId] = {
    if(!studentDir.isDirectory) {
      mutable.Set.empty
    } else {
      mutable.Set.empty ++ studentDir.listFiles.toSeq
        .filter(_.isFile)
        .map(file => PersonId(Integer.parseInt(file.getName)))
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
