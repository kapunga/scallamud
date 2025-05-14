package slm.protocol

import io.circe.{Codec, Decoder, Encoder}
import io.circe.Codec.AsObject.derivedConfigured
import io.circe.derivation.Configuration
import org.http4s.Uri

enum Entry:
  case InfoPage(description: String, page: Uri)
  case Tty(description: String, webSocket: Uri, method: Option[AuthMethod])
  case Gateway(header: String, entries: List[Entry])

object Entry:
  given Configuration = Configuration.default.withSnakeCaseMemberNames

  // URI codec that needs to be in scope where Entry is used
  given Codec[Uri] = Codec.from(
    Decoder.decodeString.emap(str => Uri.fromString(str).left.map(_.message)),
    Encoder.encodeString.contramap[Uri](_.toString)
  )

  given Codec[Entry.InfoPage] = derivedConfigured[Entry.InfoPage]
  given Codec[Entry.Tty] = derivedConfigured[Entry.Tty]
  given Codec[Entry.Gateway] = derivedConfigured[Entry.Gateway]
  
  // Entry codec for recursive references
  given Codec[Entry] = Codec.from(
    Decoder.instance { c =>
      c.as[Entry.InfoPage].map(identity)
        .orElse(c.as[Entry.Tty].map(identity))
        .orElse(c.as[Entry.Gateway].map(identity))
    },
    Encoder.instance {
      case ip: Entry.InfoPage => Encoder[Entry.InfoPage].apply(ip)
      case tty: Entry.Tty => Encoder[Entry.Tty].apply(tty)
      case gw: Entry.Gateway => Encoder[Entry.Gateway].apply(gw)
    }
  )

enum AuthMethod(val value: String):
  case Password extends AuthMethod("password")

object AuthMethod:
  given Codec[AuthMethod] = Codec.from(
    Decoder.decodeString.emap { str =>
      AuthMethod.values.find(_.value == str) match
        case Some(method) => Right(method)
        case None => Left(s"Invalid auth method: $str")
    },
    Encoder.encodeString.contramap[AuthMethod](_.value)
  )