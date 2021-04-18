package one.irradia.opds2_0.library_simplified.api

import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata

/**
 * An OPDS 2.0 catalog.
 */

data class OPDS20Catalog(

  /**
   * The catalog metadata.
   */

  val metadata: OPDS20Metadata,

  /**
   * The catalog links
   */

  val links: List<OPDS20Link>

) : OPDS20ExtensionElementType
