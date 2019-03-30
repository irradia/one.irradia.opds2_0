package one.irradia.opds2_0.parser.api

import java.io.InputStream
import java.net.URI

interface OPDS20FeedParserProviderType {

  /**
   * Create a new parser.
   */

  fun createParser(
    documentURI: URI,
    stream: InputStream)
    : OPDS20FeedParserType

}
