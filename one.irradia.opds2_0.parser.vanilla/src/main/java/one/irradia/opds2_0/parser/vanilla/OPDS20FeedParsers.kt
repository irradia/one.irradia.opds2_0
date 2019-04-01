package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.vanilla.FRParsers
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import one.irradia.opds2_0.parser.api.OPDS20FeedParserType
import java.io.InputStream
import java.net.URI

/**
 * A default provider of parsers.
 *
 * Note: This class must have a public no-argument constructor in order to be used correctly
 * from [java.util.ServiceLoader].
 */

class OPDS20FeedParsers : OPDS20FeedParserProviderType {

  private val parsers = FRParsers()

  override fun createParser(
    documentURI: URI,
    stream: InputStream): OPDS20FeedParserType {
    val parser =
      this.parsers.createParser(documentURI, stream, OPDS20ValueParserFeed())
    return OPDS20FeedParser(parser)
  }

}
