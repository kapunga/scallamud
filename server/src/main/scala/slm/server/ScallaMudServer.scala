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

package slm.server

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import org.http4s.{ EntityEncoder, HttpRoutes }
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.circe.CirceEntityEncoder.*
import slm.protocol.Entry

/** Main server class for ScallaMUD.
  *
  * This class implements a simple HTTP server using http4s and cats-effect.
  */
object ScallaMudServer extends IOApp.Simple:
  given EntityEncoder[IO, Entry] = jsonEncoderOf[IO, Entry]

  val directory = Entry.Gateway("foo", List.empty)
  
  val routes = HttpRoutes
    .of[IO] {
      case GET -> Root            => Ok(directory)
      case GET -> Root / "status" => Ok("Server is running")
    }
    .orNotFound

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
