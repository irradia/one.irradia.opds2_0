package one.irradia.opds2_0.parser.vanilla

import one.irradia.fieldrush.api.FRLexicalPosition
import one.irradia.fieldrush.api.FRParseError
import one.irradia.fieldrush.api.FRParseResult
import one.irradia.fieldrush.api.FRParserType
import one.irradia.opds2_0.api.OPDS20Feed
import one.irradia.opds2_0.api.OPDS20ParseError
import one.irradia.opds2_0.api.OPDS20ParseResult
import one.irradia.opds2_0.lexical.OPDS20LexicalPosition
import one.irradia.opds2_0.parser.api.OPDS20FeedParserType

internal class OPDS20FeedParser(
  private val parser: FRParserType<OPDS20Feed>) : OPDS20FeedParserType {

  private var closed = false

  override fun close() {
    try {
      if (!this.closed) {
        this.parser.close()
      }
    } finally {
      this.closed = true
    }
  }

  override fun parse(): OPDS20ParseResult<OPDS20Feed> {
    this.checkNotClosed()

    val result = this.parser.parse()
    return when (result) {
      is FRParseResult.FRParseSucceeded ->
        OPDS20ParseResult.succeed(result.result)
      is FRParseResult.FRParseFailed ->
        OPDS20ParseResult.OPDS20ParseFailed(result.errors.map { error -> mapError(error) })
    }
  }

  private fun mapError(error: FRParseError): OPDS20ParseError {
    return OPDS20ParseError(
      producer = error.producer,
      position = mapPosition(error.position),
      message = error.message,
      exception = error.exception)
  }

  private fun mapPosition(position: FRLexicalPosition): OPDS20LexicalPosition {
    return OPDS20LexicalPosition(
      source = position.source,
      line = position.line,
      column = position.column)
  }

  private fun checkNotClosed() {
    if (this.closed) {
      throw IllegalStateException("Parser is closed")
    }
  }
}
