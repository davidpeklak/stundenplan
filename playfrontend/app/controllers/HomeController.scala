package controllers

import java.io.File

import davidpeklak.stundenplan.logic.Logic
import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.storage.FileStorage
import javax.inject._
import models.person._
import play.api.mvc._
import util.EitherUtil._


@Singleton
class HomeController @Inject()(mcc: MessagesControllerComponents)
  extends MessagesAbstractController(mcc) {

  lazy val storageBaseDir = new File("target")
  lazy val fileStorage = new FileStorage(storageBaseDir)
  lazy val logic = new Logic(fileStorage.load, fileStorage)

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index()).withHeaders()
  }

  def setTeacherPut(id: Int) = Action { implicit request =>
    val pid = PersonId(id)
    logic.setTeacher(pid)
    val teacher = logic.getPerson(pid).get
    Ok(teacher.toJson)
  }

  def updatePersonPut(id: Int) = Action { implicit request =>
    val pid = PersonId(id)
    val person = personFromJson(request.body.asText.get)
    if (pid != person.id) throw new IllegalArgumentException("URL id does not match person id in body")
    // logic.updatePerson(person)
    val sosoPerson = logic.getPerson(person.id).get
    Ok(sosoPerson.toJson)
  }

  def createPersonPost() = Action { implicit request =>
    val result = for {
      name <- request.body.asText.toRight(BadRequest("Failed to parse http body as text"))
    } yield {
      val person = logic.createPerson(name)
      Created(person.toJson)
    }
    result.both
  }

  def getTeacher = Action {
    logic.getTeacher match {
      case None => Ok("No teacher set")
      case Some(t) =>
        Ok(t.toJson)
    }
  }
}


