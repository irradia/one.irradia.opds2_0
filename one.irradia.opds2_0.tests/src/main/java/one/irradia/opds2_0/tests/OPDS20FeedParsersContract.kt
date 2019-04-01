package one.irradia.opds2_0.tests

import one.irradia.mime.vanilla.MIMEParser
import one.irradia.opds2_0.api.OPDS20ParseResult
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import org.hamcrest.core.StringContains
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
  fun testOPDS20TestCatalogHome()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("test-catalog/2.0/home.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assert.assertEquals("OPDS 2.0 Test Catalog", feed.metadata.title.title)
    Assert.assertEquals(URI.create("https://test.opds.io/2.0/home.json"), feed.links[0].href)
    Assert.assertEquals(MIMEParser.parseRaisingException("application/opds+json"), feed.links[0].type)
    Assert.assertEquals("self", feed.links[0].relations[0])

    Assert.assertTrue(feed.navigation != null)
    val nav = feed.navigation!!
    Assert.assertEquals(8, nav.links.size)

    Assert.assertEquals("https://test.opds.io/2.0/navigation.json", nav.links[0].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/publications.json", nav.links[1].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[2].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[3].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[4].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[5].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[6].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[7].href.toString())

    Assert.assertEquals("application/opds+json", nav.links[0].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[1].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[2].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[3].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[4].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[5].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[6].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[7].type!!.fullType)

    Assert.assertEquals("Explore (Navigation Feed)", nav.links[0].title)
    Assert.assertEquals("French Books (Publications Feed)", nav.links[1].title)
    Assert.assertEquals("Third Link", nav.links[2].title)
    Assert.assertEquals("Fourth Link", nav.links[3].title)
    Assert.assertEquals("Fifth Link", nav.links[4].title)
    Assert.assertEquals("Sixth Link", nav.links[5].title)
    Assert.assertEquals("Seventh Link", nav.links[6].title)
    Assert.assertEquals("Eight Link", nav.links[7].title)
  }

  @Test
  fun testOPDS20TestCatalogNavigation()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("test-catalog/2.0/navigation.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assert.assertEquals("OPDS 2.0 Test Navigation", feed.metadata.title.title)
    Assert.assertEquals(URI.create("https://test.opds.io/2.0/navigation.json"), feed.links[0].href)
    Assert.assertEquals(MIMEParser.parseRaisingException("application/opds+json"), feed.links[0].type)
    Assert.assertEquals("self", feed.links[0].relations[0])

    Assert.assertTrue(feed.navigation != null)
    val nav = feed.navigation!!
    Assert.assertEquals(5, nav.links.size)

    Assert.assertEquals("https://test.opds.io/2.0/home.json", nav.links[0].href.toString())
    Assert.assertEquals("home.json", nav.links[1].href.toString())
    Assert.assertEquals("/2.0/home.json", nav.links[2].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/404.json", nav.links[3].href.toString())
    Assert.assertEquals("https://test.opds.io/2.0/navigation.json", nav.links[4].href.toString())

    Assert.assertEquals("application/opds+json", nav.links[0].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[1].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[2].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[3].type!!.fullType)
    Assert.assertEquals("application/opds+json", nav.links[4].type!!.fullType)

    Assert.assertEquals("Best Selling (Absolute URI)", nav.links[0].title)
    Assert.assertEquals("New Releases (Relative URI)", nav.links[1].title)
    Assert.assertEquals("Staff Picks (Also Relative)", nav.links[2].title)
    Assert.assertEquals("Categories (404)", nav.links[3].title)
    Assert.assertEquals("Explore (Navigation Feed)", nav.links[4].title)
  }

  @Test
  fun testOPDS20TestCatalogPublications()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("test-catalog/2.0/publications.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result
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
  fun testNoSelfLink()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("no-self-link.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assert.assertEquals(1, failure.errors.size)
    Assert.assertThat(failure.errors[0].message, StringContains("self"))
  }

  @Test
  fun testNoMetadataTitle()
  {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("no-metadata-title.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assert.assertEquals(1, failure.errors.size)
    Assert.assertThat(failure.errors[0].message, StringContains("title"))
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
