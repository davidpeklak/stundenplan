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


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(mcc: MessagesControllerComponents)
  extends MessagesAbstractController(mcc) {

  lazy val storageBaseDir = new File("target")
  lazy val fileStorage = new FileStorage(storageBaseDir)
  lazy val logic = new Logic(fileStorage.load, fileStorage)

  private val personForm = Form(
    mapping(
      "name" -> text,
    )(Person.apply)(Person.unapply)
  )

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index(personForm)).withHeaders()
  }

  def teacherPost = Action(parse.form(personForm)) { implicit request =>
    val person = request.body
    logic.setTeacher(person)
    Redirect(routes.HomeController.teacher)
  }

  def teacher = Action {
    logic.getTeacher match {
      case None => Ok("No teacher set")
      case Some(t) => {
        import play.api.libs.json._

        implicit val residentWrites = Json.writes[Person]
        val teacherJson = Json.toJson(t)

        Ok(teacherJson)
      }
    }
  }
}
