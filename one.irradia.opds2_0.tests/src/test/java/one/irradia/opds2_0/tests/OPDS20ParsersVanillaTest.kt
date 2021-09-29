package one.irradia.opds2_0.tests

import one.irradia.opds2_0.api.OPDS20ParseResult
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogList
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogMetadata
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import one.irradia.opds2_0.parser.extension.library_simplified.OPDS20CatalogExtension
import one.irradia.opds2_0.parser.vanilla.OPDS20FeedParsers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

class OPDS20ParsersVanillaTest {

  private val logger =
    LoggerFactory.getLogger(OPDS20ParsersVanillaTest::class.java)

  private lateinit var parsers: OPDS20FeedParserProviderType

  private fun resource(name: String): InputStream {
    val path = "/one/irradia/opds2_0/tests/$name"
    val url =
      OPDS20ParsersVanillaTest::class.java.getResource(path)
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
    this.parsers = OPDS20FeedParsers.createWithExtensions(listOf(OPDS20CatalogExtension()))
  }

  @Test
  fun testLibraryRegistry() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("library-registry.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(3, success.warnings.size)
    assertTrue(success.warnings[0].message.contains("broken URI"))
    assertTrue(success.warnings[1].message.contains("broken URI"))
    assertTrue(success.warnings[2].message.contains("broken URI"))

    val feed = success.result
    val catalogs = feed.extensions.find { it is OPDS20CatalogList } as OPDS20CatalogList
    assertEquals(356, catalogs.catalogs.size)

    for (catalog in catalogs.catalogs) {
      this.logger.debug("{}", catalog.metadata.title.title)
    }

    assertAll(catalogs.catalogs.map {
      {
        val catalogMetadata = it.metadata.extensionOf(OPDS20CatalogMetadata::class.java)!!
        assertNotNull(catalogMetadata.updated)
      }
    })
  }

  @Test
  fun testLibraryRegistryEmpty() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("library-registry-empty.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)

    val feed = success.result
    val catalogs = feed.extensions.find { it is OPDS20CatalogList } as OPDS20CatalogList
    assertEquals(0, catalogs.catalogs.size)
  }

  @Test
  fun testLibraryRegistryExtended() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("library-registry-extended.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)

    val feed = success.result
    val catalogs = feed.extensions.find { it is OPDS20CatalogList } as OPDS20CatalogList
    assertEquals(1, catalogs.catalogs.size)

    val catalog = catalogs.catalogs[0]
    assertEquals("Auto Production", catalog.metadata.title.title)
    val extras = catalog.metadata.extensions.find { it is OPDS20CatalogMetadata } as OPDS20CatalogMetadata
    assertTrue(extras.isAutomatic)
    assertTrue(extras.isProduction)
    assertEquals(URI.create("urn:uuid:25cb02b7-4431-4c86-b1b7-7dcbda353e04"), extras.id)
  }

  @Test
  fun testLibraryRegistryExtendedLocation() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("library-registry-extended-location.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)

    val feed = success.result
    val catalogs = feed.extensionOf(OPDS20CatalogList::class.java)!!
    val catalog = catalogs.catalogs[0]
    assertEquals("Auto Production", catalog.metadata.title.title)
    val extras = catalog.metadata.extensionOf(OPDS20CatalogMetadata::class.java)!!
    assertTrue(extras.isAutomatic)
    assertTrue(extras.isProduction)
    assertEquals("40.753141642210466, -73.98229631746968", extras.areaServed)
    assertEquals("128 km", extras.distance)
    assertEquals(URI.create("urn:uuid:25cb02b7-4431-4c86-b1b7-7dcbda353e04"), extras.id)
  }

  @Test
  fun testSample0() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("sample-0.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)
  }

  @Test
  fun testSample0Unparseable() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("sample-0-unparseable.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val failed = result as OPDS20ParseResult.OPDS20ParseFailed
    assertEquals(0, failed.warnings.size)
    assertEquals(3, failed.errors.size)
  }

  @Test
  fun testSample1() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("sample-1.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)

    val catalogs = success.result.extensionOf(OPDS20CatalogList::class.java)!!
    val catalog = catalogs.catalogs[0]
    val catalogMeta = catalog.metadata.extensionOf(OPDS20CatalogMetadata::class.java)
    assertEquals("14 km.", catalogMeta!!.distance)
    assertEquals("Kings County, NY", catalogMeta.areaServed)
  }

  @Test
  fun testLibraryRegistryLyrasis() {
    val parser =
      this.parsers.createParser(
        documentURI = URI.create("urn:test"),
        stream = resource("lyrasis-registry.json"))

    val result = parser.parse()
    this.dumpParseResult(result)

    val success = result as OPDS20ParseResult.OPDS20ParseSucceeded
    assertEquals(0, success.warnings.size)

    val feed = success.result
    val catalogs = feed.extensions.find { it is OPDS20CatalogList } as OPDS20CatalogList

    for (catalog in catalogs.catalogs) {
      assertNotNull(catalog.metadata.description)
    }
  }
}
