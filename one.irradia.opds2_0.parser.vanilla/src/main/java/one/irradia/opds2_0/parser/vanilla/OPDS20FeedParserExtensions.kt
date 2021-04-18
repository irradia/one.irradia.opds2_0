package one.irradia.opds2_0.parser.vanilla

import one.irradia.opds2_0.parser.extension.spi.OPDS20FeedRoleExtensionType

/**
 * Extensions for the feed parser.
 */

data class OPDS20FeedParserExtensions(
  val feedRoleExtensions: List<OPDS20FeedRoleExtensionType<*>> = listOf()
)
