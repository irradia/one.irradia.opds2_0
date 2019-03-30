package one.irradia.opds2_0.api

import one.irradia.mime.api.MIMEType
import java.net.URI

/**
 * A generic OPDS 2.0 link.
 */

data class OPDS20Link(

  /**
   * The target of the link.
   */

  val href: URI,

  /**
   * The declared MIME type of the link, if any.
   */

  val type: MIMEType?,

  /**
   * The declared link relation, if any.
   */

  val relation: String?): OPDS20ElementType

