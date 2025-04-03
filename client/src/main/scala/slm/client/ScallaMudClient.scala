package slm.client

import java.net.URL

import scala.util.Try

import cats.effect.{ ExitCode, IO, IOApp }
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.{Method, Request, Uri}

/** Main client class for ScallaMUD.
  *
  * This client connects to a specified server URL or defaults to
  * localhost:8080.
  */
object ScallaMudClient extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    val serverUrl = args.headOption
      .map(url => validateUrl(url).getOrElse("http://localhost:8080"))
      .getOrElse("http://localhost:8080")

    for
      _ <- IO.println(s"Starting ScallaMUD client connecting to $serverUrl...")
      exitCode <- EmberClientBuilder
        .default[IO]
        .build
        .use { client =>
          for
            _ <- IO.println(s"Sending request to $serverUrl")
            response <- fetchFromServer(client, serverUrl)
            _ <- IO.println("Response received:")
            _ <- IO.println(response)
          yield ExitCode.Success
        }
    yield exitCode

  private def validateUrl(url: String): Option[String] =
    Try(new URL(url)).map(_.toString).toOption

  private def fetchFromServer(client: Client[IO], url: String): IO[String] =
    val request = Request[IO](Method.GET, Uri.unsafeFromString(url))
    client.expect[String](request)
