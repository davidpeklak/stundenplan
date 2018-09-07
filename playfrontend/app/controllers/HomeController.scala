package controllers

import java.io.File

import davidpeklak.stundenplan.logic.Logic
import davidpeklak.stundenplan.person.Person
import davidpeklak.stundenplan.storage.FileStorage
import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n._
import models.person._


@Singleton
class HomeController @Inject()(mcc: MessagesControllerComponents)
  extends MessagesAbstractController(mcc) {

  lazy val storageBaseDir = new File("target")
  lazy val fileStorage = new FileStorage(storageBaseDir)
  lazy val logic = new Logic(fileStorage.load, fileStorage)

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index()).withHeaders()
  }

  def teacherPost = Action { implicit request =>
    val person = personFromJson(request.body.asText.get)

    Ok("oida")
  }

  def teacher = Action {
    logic.getTeacher match {
      case None => Ok("No teacher set")
      case Some(t) => {
        Ok(t.toJson)
      }
    }
  }
}


