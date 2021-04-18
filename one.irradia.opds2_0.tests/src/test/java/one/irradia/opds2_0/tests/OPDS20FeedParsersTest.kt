package one.irradia.opds2_0.tests

import one.irradia.opds2_0.parser.api.OPDS20FeedParserProviderType
import one.irradia.opds2_0.parser.vanilla.OPDS20FeedParsers
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OPDS20FeedParsersTest : OPDS20FeedParsersContract() {

  override fun logger(): Logger {
    return LoggerFactory.getLogger(OPDS20FeedParsersTest::class.java)
  }

  override fun parsers(): OPDS20FeedParserProviderType {
    return OPDS20FeedParsers.createWithoutExtensions()
  }
}
