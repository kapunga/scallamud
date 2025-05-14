package slm.protocol

import io.circe.{Decoder, Encoder, Json}
import io.circe.parser.decode
import io.circe.syntax.*
import munit.FunSuite
import org.http4s.Uri

class EntryTest extends FunSuite:
  // Helper methods for testing JSON encoding/decoding
  def encodeAndDecode[A: Encoder: Decoder](value: A): A =
    decode[A](value.asJson.noSpaces).getOrElse(fail("Failed to decode JSON"))

  def encodeAndDecodeObject[A: Encoder: Decoder](value: A, expectedJson: Json): A =
    val encoded = value.asJson
    assertEquals(encoded, expectedJson)
    decode[A](encoded.noSpaces).getOrElse(fail("Failed to decode JSON"))

  // Test InfoPage encoding/decoding
  test("InfoPage should encode to and decode from JSON correctly") {
    val uri = Uri.unsafeFromString("https://example.com/info")
    val infoPage = Entry.InfoPage("Test Info Page", uri)
    
    val expectedJson = Json.obj(
      "description" -> Json.fromString("Test Info Page"),
      "page" -> Json.fromString("https://example.com/info")
    )
    
    val roundTrip = encodeAndDecodeObject(infoPage, expectedJson)
    assertEquals(roundTrip, infoPage)
  }

  // Test Tty encoding/decoding with method
  test("Tty with password method should encode to and decode from JSON correctly") {
    val uri = Uri.unsafeFromString("wss://example.com/terminal")
    val tty = Entry.Tty("Test Terminal", uri, Some(AuthMethod.Password))
    
    val expectedJson = Json.obj(
      "description" -> Json.fromString("Test Terminal"),
      "web_socket" -> Json.fromString("wss://example.com/terminal"),
      "method" -> Json.fromString("password")
    )
    
    val roundTrip = encodeAndDecodeObject(tty, expectedJson)
    assertEquals(roundTrip, tty)
  }

  // Test Tty encoding/decoding without method
  test("Tty without method should encode to and decode from JSON correctly") {
    val uri = Uri.unsafeFromString("wss://example.com/terminal")
    val tty = Entry.Tty("Test Terminal", uri, None)
    
    val expectedJson = Json.obj(
      "description" -> Json.fromString("Test Terminal"),
      "web_socket" -> Json.fromString("wss://example.com/terminal"),
      "method" -> Json.Null
    )
    
    val roundTrip = encodeAndDecodeObject(tty, expectedJson)
    assertEquals(roundTrip, tty)
  }

  // Test Gateway encoding/decoding
  test("Gateway should encode to and decode from JSON correctly") {
    val infoUri = Uri.unsafeFromString("https://example.com/info")
    val ttyUri = Uri.unsafeFromString("wss://example.com/terminal")
    
    val entries = List(
      Entry.InfoPage("Info Page", infoUri),
      Entry.Tty("Terminal", ttyUri, Some(AuthMethod.Password))
    )
    
    val gateway = Entry.Gateway("Test Gateway", entries)
    
    val expectedJson = Json.obj(
      "header" -> Json.fromString("Test Gateway"),
      "entries" -> Json.arr(
        Json.obj(
          "description" -> Json.fromString("Info Page"),
          "page" -> Json.fromString("https://example.com/info")
        ),
        Json.obj(
          "description" -> Json.fromString("Terminal"),
          "web_socket" -> Json.fromString("wss://example.com/terminal"),
          "method" -> Json.fromString("password")
        )
      )
    )
    
    val roundTrip = encodeAndDecodeObject(gateway, expectedJson)
    assertEquals(roundTrip, gateway)
  }

  // Test AuthMethod encoding/decoding
  test("AuthMethod should encode to and decode from JSON correctly") {
    val method = AuthMethod.Password
    val encodedString = method.asJson.noSpaces
    
    assertEquals(encodedString, "\"password\"")
    
    val decoded = decode[AuthMethod](encodedString).getOrElse(fail("Failed to decode JSON"))
    assertEquals(decoded, method)
  }

  // Test invalid AuthMethod decoding
  test("Invalid AuthMethod should fail to decode") {
    val result = decode[AuthMethod]("\"invalid_method\"")
    assert(result.isLeft)
  }

  // Test Entry polymorphic encoding/decoding
  test("Entry should handle polymorphic encoding/decoding") {
    val infoUri = Uri.unsafeFromString("https://example.com/info")
    val ttyUri = Uri.unsafeFromString("wss://example.com/terminal")
    
    val entries: List[Entry] = List(
      Entry.InfoPage("Info Page", infoUri),
      Entry.Tty("Terminal", ttyUri, Some(AuthMethod.Password)),
      Entry.Gateway("Nested Gateway", List(Entry.InfoPage("Nested Info", infoUri)))
    )
    
    for entry <- entries do
      val roundTrip = encodeAndDecode[Entry](entry)
      assertEquals(roundTrip, entry)
  }