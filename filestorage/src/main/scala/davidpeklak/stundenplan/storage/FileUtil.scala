package davidpeklak.stundenplan.storage

import java.io.File

object FileUtil {

  implicit class FileOps(file: File) {
    def / (child: String): File = {
      new File(file, child)
    }
  }

  def tryMkDirs(dir: File): File = {
    if (dir.exists && dir.isDirectory) {
      dir
    }
    else {
      if (dir.mkdirs()) {
        dir
      } else {
        throw new Exception("Failed to create path $dir")
      }
    }
  }

}
