package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.vanilla.FRParsers
import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import one.irradia.opds2_0.parser.api.OPDS20FeedParserType
import java.io.InputStream
import java.net.URI

/**
 * A default provider of parsers.
 */

class OPDS20FeedParsers private constructor(
  private val extensions: OPDS20FeedParserExtensions
) : OPDS20FeedParserProviderType {

  private val parsers = FRParsers()

  companion object {

    /**
     * Create a parser that does not use any extensions.
     */

    fun createWithoutExtensions(): OPDS20FeedParserProviderType {
      return OPDS20FeedParsers(OPDS20FeedParserExtensions())
    }

    /**
     * Create a parser that uses the provided extensions.
     */

    fun createWithExtensions(
      extensions: OPDS20FeedParserExtensions
    ): OPDS20FeedParserProviderType {
      return OPDS20FeedParsers(extensions)
    }
  }

  override fun createParser(
    documentURI: URI,
    stream: InputStream
  ): OPDS20FeedParserType {
    return OPDS20FeedParser(
      this.parsers.createParser(documentURI, stream, OPDS20ValueParserFeed(this.extensions))
    )
  }
}
