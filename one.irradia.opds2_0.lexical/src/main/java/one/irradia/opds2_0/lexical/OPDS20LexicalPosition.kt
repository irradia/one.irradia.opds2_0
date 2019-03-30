package one.irradia.opds2_0.lexical

import java.net.URI

/**
 * A lexical position of an element in a document.
 */

data class OPDS20LexicalPosition(

  /**
   * The source URI of the document.
   */

  val source: URI,

  /**
   * The line number.
   */

  val line: Int,

  /**
   * The column number.
   */

  val column: Int)
