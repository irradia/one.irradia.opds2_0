package one.irradia.opds2_0.api

import one.irradia.opds2_0.lexical.OPDS20LexicalPosition

/**
 * A specific parse error.
 */

data class OPDS20ParseError(

  /**
   * The parser that produced the error. This will typically be the name of the core parser, or
   * the name of one of the extension parsers.
   */

  val producer: String,

  /**
   * Lexical information for the parse error.
   */

  val position: OPDS20LexicalPosition,

  /**
   * The error message.
   */

  val message: String,

  /**
   * The exception raised, if any.
   */

  val exception: Exception?)