package one.irradia.opds2_0.tests

import one.irradia.mime.vanilla.MIMEParser
import one.irradia.opds2_0.api.OPDS20ParseResult
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.slf4j.Logger
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.IllegalStateException
import java.net.URI

abstract class OPDS20FeedParsersContract {

  abstract fun parsers(): OPDS20FeedParserProviderType

  abstract fun logger(): Logger

  private lateinit var parsers: OPDS20FeedParserProviderType
  private lateinit var logger: Logger

  @JvmField
  @Rule
  val expected = ExpectedException.none()

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds2_0/tests/$name"
    val url =
      OPDS20FeedParsersContract::class.java.getResource(path)
        ?: throw FileNotFoundException("No such resource: $path")
    return url.openStream()
  }

  private fun <T> dumpParseResult(result: OPDS20ParseResult<T>) {
    return when (result) {
      is OPDS20ParseResult.OPDS20ParseSucceeded -> {
        this.logger.debug("success: {}", result.result)
      }
      is OPDS20ParseResult.OPDS20ParseFailed -> {
        result.errors.forEach { error ->
          this.logger.debug("error: {}: ", error, error.exception)
        }
      }
    }
  }

  @Before
  fun testSetup()
  {
    this.parsers = this.parsers()
    this.logger = this.logger()
  }

  @Test
  fun testEmpty()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assert.assertEquals(2, failure.errors.size)
  }

  @Test
  fun testGardeurTestCatalog0()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("gardeur-test-catalog-0.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assert.assertEquals("OPDS 2.0 Test Catalog", feed.metadata.title)
    Assert.assertEquals(URI.create("https://test.opds.io/2.0/home.json"), feed.links[0].href)
    Assert.assertEquals(MIMEParser.parseRaisingException("application/opds+json"), feed.links[0].type)
    Assert.assertEquals("self", feed.links[0].relation)
  }

  @Test
  fun testEmptyArray()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty-array.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assert.assertEquals(1, failure.errors.size)
  }

  @Test
  fun testParseClosed()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty-array.json"))

    parser.close()
    this.expected.expect(IllegalStateException::class.java)
    parser.parse()
  }
}
