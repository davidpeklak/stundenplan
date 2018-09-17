package controllers

import java.io.File

import davidpeklak.stundenplan.logic.Logic
import davidpeklak.stundenplan.person.{Person, PersonId}
import davidpeklak.stundenplan.storage.FileStorage
import javax.inject._
import models.person._
import play.api.mvc._
import util.EitherUtil._


import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(mcc: MessagesControllerComponents)
  extends MessagesAbstractController(mcc) {

  lazy val storageBaseDir = new File("target")
  lazy val fileStorage = new FileStorage(storageBaseDir)
  lazy val logic = new Logic(fileStorage.load, fileStorage)

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index()).withHeaders()
  }

  def getPerson(id: Int) = Action { implicit request =>
    val result = for {
      person <- logic.getPerson(PersonId(id)).toRight(NotFound(s"A Person with id $id does not exist"))
    } yield {
      Ok(person.toJson).withHeaders(
        CONTENT_TYPE -> JSON,
        "EsJusto" -> "ybueno"
      )
    }
    result.both
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

  private def json4s(implicit ec: ExecutionContext): BodyParser[org.json4s.JValue] = json4s(parse.DefaultMaxTextLength)

  private def json4s(maxLength: Int)(implicit ec: ExecutionContext): BodyParser[org.json4s.JValue] = parse.when(
    _.contentType.exists(m => m.equalsIgnoreCase("text/json") || m.equalsIgnoreCase("application/json")),
    tolerantJson4s(maxLength),
    _ => Future(UnsupportedMediaType("Expecting text/json or application/json body")
  ))

  private def tolerantJson4s(maxLength: Int)(implicit ec: ExecutionContext): BodyParser[org.json4s.JValue] = {
  parse.tolerantText(maxLength).map(string => org.json4s.native.JsonMethods.parse(string))
  }

  def updatePersonPut(id: Int) = Action(json4s(scala.concurrent.ExecutionContext.Implicits.global)) { implicit request =>
    val pid = PersonId(id)
    val person = personFromJson(request.body)
    if (pid != person.id) throw new IllegalArgumentException("URL id does not match person id in body")
    logic.updatePerson(person)
    Ok(person.toJson)
  }

  def deletePerson(id: Int) = Action { implicit request =>
    logic.deletePerson(PersonId(id))
    Ok("ok")
  }

  def addStudent(id: Int) = Action { implicit request =>
    logic.addStudent(PersonId(id))
    Ok("ok")
  }

  def removeStudent(id: Int) = Action { implicit request =>
    logic.removeStudent(PersonId(id))
    Ok("ok")
  }

  def setTeacherPut(id: Int) = Action { implicit request =>
    val pid = PersonId(id)
    logic.setTeacher(pid)
    val teacher = logic.getPerson(pid).get
    Ok(teacher.toJson)
  }

  def getTeacher = Action {
    logic.getTeacher match {
      case None => Ok("No teacher set")
      case Some(t) =>
        Ok(t.toJson)
    }
  }
}


