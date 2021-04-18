package one.irradia.opds2_0.parser.api

import one.irradia.opds2_0.api.OPDS20Feed
import one.irradia.opds2_0.api.OPDS20ParseResult
import java.io.Closeable

/**
 * An instance of a feed parser.
 */

interface OPDS20FeedParserType : Closeable {

  /**
   * Parse the current element, returning results or errors.
   */

  fun parse(): OPDS20ParseResult<OPDS20Feed>

}