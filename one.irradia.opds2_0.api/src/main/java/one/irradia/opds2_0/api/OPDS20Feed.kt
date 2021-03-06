package one.irradia.opds2_0.api

import java.net.URI

/**
 * An OPDS 2.0 feed.
 */

data class OPDS20Feed(

  /**
   * The URI of the feed.
   */

  val uri: URI,

  /**
   * The feed metadata.
   */

  val metadata: OPDS20Metadata,

  /**
   * The navigation section, if any.
   */

  val navigation: OPDS20Navigation?,

  /**
   * The feed links
   */

  val links: List<OPDS20Link>): OPDS20ElementType
