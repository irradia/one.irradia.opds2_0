package one.irradia.opds2_0.tests

import one.irradia.mime.vanilla.MIMEParser
import one.irradia.opds2_0.api.OPDS20ParseResult
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

abstract class OPDS20FeedParsersContract {

  abstract fun parsers(): OPDS20FeedParserProviderType

  abstract fun logger(): Logger

  private lateinit var parsers: OPDS20FeedParserProviderType
  private lateinit var logger: Logger

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

  @BeforeEach
  fun testSetup() {
    this.parsers = this.parsers()
    this.logger = this.logger()
  }

  @Test
  fun testEmpty() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assertions.assertEquals(1, failure.errors.size)
  }

  @Test
  fun testOPDS20TestCatalogHome() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("test-catalog/2.0/home.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assertions.assertEquals("OPDS 2.0 Test Catalog", feed.metadata.title.title)
    Assertions.assertEquals(URI.create("https://test.opds.io/2.0/home.json"), feed.links[0].hrefURI)
    Assertions.assertEquals(MIMEParser.parseRaisingException("application/opds+json"), feed.links[0].type)
    Assertions.assertEquals("self", feed.links[0].relations[0])

    Assertions.assertTrue(feed.navigation != null)
    val nav = feed.navigation!!
    Assertions.assertEquals(8, nav.links.size)

    Assertions.assertEquals("https://test.opds.io/2.0/navigation.json", nav.links[0].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/publications.json", nav.links[1].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[2].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[3].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[4].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[5].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[6].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[7].hrefURI.toString())

    Assertions.assertEquals("application/opds+json", nav.links[0].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[1].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[2].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[3].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[4].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[5].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[6].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[7].type!!.fullType)

    Assertions.assertEquals("Explore (Navigation Feed)", nav.links[0].title)
    Assertions.assertEquals("French Books (Publications Feed)", nav.links[1].title)
    Assertions.assertEquals("Third Link", nav.links[2].title)
    Assertions.assertEquals("Fourth Link", nav.links[3].title)
    Assertions.assertEquals("Fifth Link", nav.links[4].title)
    Assertions.assertEquals("Sixth Link", nav.links[5].title)
    Assertions.assertEquals("Seventh Link", nav.links[6].title)
    Assertions.assertEquals("Eight Link", nav.links[7].title)
  }

  @Test
  fun testOPDS20TestCatalogNavigation() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("test-catalog/2.0/navigation.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assertions.assertEquals("OPDS 2.0 Test Navigation", feed.metadata.title.title)
    Assertions.assertEquals(URI.create("https://test.opds.io/2.0/navigation.json"), feed.links[0].hrefURI)
    Assertions.assertEquals(MIMEParser.parseRaisingException("application/opds+json"), feed.links[0].type)
    Assertions.assertEquals("self", feed.links[0].relations[0])

    Assertions.assertTrue(feed.navigation != null)
    val nav = feed.navigation!!
    Assertions.assertEquals(5, nav.links.size)

    Assertions.assertEquals("https://test.opds.io/2.0/home.json", nav.links[0].hrefURI.toString())
    Assertions.assertEquals("home.json", nav.links[1].hrefURI.toString())
    Assertions.assertEquals("/2.0/home.json", nav.links[2].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/404.json", nav.links[3].hrefURI.toString())
    Assertions.assertEquals("https://test.opds.io/2.0/navigation.json", nav.links[4].hrefURI.toString())

    Assertions.assertEquals("application/opds+json", nav.links[0].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[1].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[2].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[3].type!!.fullType)
    Assertions.assertEquals("application/opds+json", nav.links[4].type!!.fullType)

    Assertions.assertEquals("Best Selling (Absolute URI)", nav.links[0].title)
    Assertions.assertEquals("New Releases (Relative URI)", nav.links[1].title)
    Assertions.assertEquals("Staff Picks (Also Relative)", nav.links[2].title)
    Assertions.assertEquals("Categories (404)", nav.links[3].title)
    Assertions.assertEquals("Explore (Navigation Feed)", nav.links[4].title)
  }

  @Test
  fun testOPDS20TestCatalogPublications() {
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
  fun testGardeurTestCatalog() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("gardeur-test-catalog-0.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    val feed = success.result

    Assertions.assertEquals(1, feed.links.size)
    Assertions.assertEquals(2, feed.publications.size)
    Assertions.assertEquals(3, feed.groups.size)

    Assertions.assertEquals(URI("https://test.opds.io/2.0/home.json"), feed.links[0].hrefURI)

    feed.groups[0].apply {
      Assertions.assertEquals("French Classics", this.metadata.title.title)
      Assertions.assertEquals(1, this.links.size)
      Assertions.assertEquals(5, this.publications.size)
      Assertions.assertEquals(0, this.navigation!!.links.size)
    }

    feed.groups[1].apply {
      Assertions.assertEquals("More Navigation", this.metadata.title.title)
      Assertions.assertEquals(0, this.links.size)
      Assertions.assertEquals(0, this.publications.size)
      Assertions.assertEquals(2, this.navigation!!.links.size)
    }

    feed.groups[2].apply {
      Assertions.assertEquals("English Classics", this.metadata.title.title)
      Assertions.assertEquals(0, this.links.size)
      Assertions.assertEquals(3, this.publications.size)
      Assertions.assertEquals(0, this.navigation!!.links.size)
    }
  }

  @Test
  fun testEmptyArray() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty-array.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assertions.assertEquals(1, failure.errors.size)
  }

  @Test
  fun testNoSelfLink() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("no-self-link.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assertions.assertEquals(1, failure.errors.size)
    Assertions.assertTrue(failure.errors[0].message.contains("self"))
  }

  @Test
  fun testNoMetadataTitle() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("no-metadata-title.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failure = result as OPDS20ParseResult.OPDS20ParseFailed
    Assertions.assertEquals(1, failure.errors.size)
    Assertions.assertTrue(failure.errors[0].message.contains("title"))
  }

  @Test
  fun testParseClosed() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("empty-array.json"))

    parser.close()

    Assertions.assertThrows(IllegalStateException::class.java) {
      parser.parse()
    }
  }

  @Test
  fun testLibraryRegistry() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("library-registry.json"))

    val result = parser.parse()
    this.dumpParseResult(result)
  }
}
