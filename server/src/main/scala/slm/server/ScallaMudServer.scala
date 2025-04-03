package slm.server

import cats.effect.{IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._

/** Main server class for ScallaMUD.
  *
  * This class implements a simple HTTP server using http4s and cats-effect.
  */
object ScallaMudServer extends IOApp.Simple:
  
  val routes = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Welcome to ScallaMUD!")
    case GET -> Root / "status" => Ok("Server is running")
  }.orNotFound

  def run: IO[Unit] =
    for
      _ <- IO.println("Starting ScallaMUD server...")
      server <- EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(routes)
        .build
        .use(_ => IO.never)
        .start
      _ <- IO.println("Server started on port 8080")
      _ <- server.join
    yield ()