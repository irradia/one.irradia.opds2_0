package one.irradia.opds2_0.api

import one.irradia.opds2_0.lexical.OPDS20LexicalPosition

/**
 * A specific parse warning.
 */

data class OPDS20ParseWarning(

  /**
   * The parser that produced the warning. This will typically be the name of the core parser, or
   * the name of one of the extension parsers.
   */

  val producer: String,

  /**
   * Lexical information for the parse warning.
   */

  val position: OPDS20LexicalPosition,

  /**
   * The warning message.
   */

  val message: String,

  /**
   * The exception raised, if any.
   */

  val exception: Exception?
)
