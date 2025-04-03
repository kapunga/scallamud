/*
 * Copyright (c) 2025 Paul (Thor) Thordarson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package slm.client

import java.net.URL

import scala.util.Try

import cats.effect.{ ExitCode, IO, IOApp }
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.{ Method, Request, Uri }

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
