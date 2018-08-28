package davidpeklak.stundenplan.storage
import java.io.{File, PrintWriter}

import davidpeklak.stundenplan.logic.State
import davidpeklak.stundenplan.person.Person

import scala.io.{Codec, Source}

class FileStorage(baseDir: File) extends Storage {
  def storeTeacher(person: Person): Unit = {
    teacherFile.delete()
    teacherFile.createNewFile()
    val pw = new PrintWriter(teacherFile, "UTF-8")
    pw.write(person.name)
    pw.close()
  }

  def load: State = {
    val teacher = loadTeacherOpt()
    val state = new State()
    state.teacher = teacher
    state
  }

  private lazy val teacherFile = new File(baseDir, "teacher")

  private def loadTeacherOpt(): Option[Person] = {
    if (!teacherFile.canRead) {
      None
    } else {
      val string = Source.fromFile( teacherFile )(Codec.UTF8).mkString
      if (string.isEmpty) {
        None
      } else {
        Some(Person(string))
      }
    }
  }
}
