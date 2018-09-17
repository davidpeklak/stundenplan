package davidpeklak.stundenplan

import java.io.{ByteArrayInputStream, File}

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{HttpOrigin, `Access-Control-Allow-Origin`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Headers`, `Content-Type`}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix, put, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.model.HttpMethods.{OPTIONS, PUT}
import akka.util.ByteString
import davidpeklak.stundenplan.logic.Logic
import davidpeklak.stundenplan.person.Person
import davidpeklak.stundenplan.storage.FileStorage

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}


object Main extends App {

  lazy val storageBaseDir = new File("target")
  lazy val fileStorage = new FileStorage(storageBaseDir)
  lazy val logic = new Logic(fileStorage.load, fileStorage)

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val acaoHeader = `Access-Control-Allow-Origin`(HttpOrigin("http://localhost:4200"))
  private val acahHeader = `Access-Control-Allow-Headers`(`Content-Type`.name)

  private val getTeacher: Route =
    get {
      path("teacher") {
        respondWithHeaders(acaoHeader) {
          import davidpeklak.stundenplan.serde.person._
          logic.getTeacher match {
            case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "teacher not set"))
            case Some(teacher) => complete(HttpEntity(ContentTypes.`application/json`, teacher.toJson))
          }
        }
      }
    }

  private val getStudents: Route =
    get {
      path("students") {
        respondWithHeaders(acaoHeader) {
          import davidpeklak.stundenplan.serde.students._
          val students = logic.getStudents
          complete(HttpEntity(ContentTypes.`application/json`, students.toJson))
        }
      }
    }

  private implicit val personUnmarshaller: Unmarshaller[HttpEntity, Person] =
    Unmarshaller.byteStringUnmarshaller.map((bs: ByteString) => {
      val inputStream = new ByteArrayInputStream(bs.toArray)
      serde.person.personFromJasonInputStream(inputStream)
    })

  private def personPrefligth: Route =
    options {
      pathPrefix("person" / IntNumber) { id =>
        complete(HttpResponse(StatusCodes.OK).
          withHeaders(`Access-Control-Allow-Methods`(OPTIONS, PUT), acaoHeader, acahHeader))
      }
    }

  private val putPerson: Route =
    put {
      pathPrefix("person" / IntNumber) { id =>
        respondWithHeaders(acaoHeader) {
          entity(as[Person]) { person =>
            import davidpeklak.stundenplan.serde.person._
            logic.updatePerson(person)
            complete(HttpEntity(ContentTypes.`application/json`, person.toJson.toString))
          }
        }
      }
    }
  
  private val route = getTeacher ~ getStudents ~ putPerson ~ personPrefligth

  val bindingFuture = Http().bindAndHandle(route, "localhost", 9000)

}
