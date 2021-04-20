package one.irradia.opds2_0.library_simplified.api

import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import one.irradia.opds2_0.api.OPDS20Link
import one.irradia.opds2_0.api.OPDS20Metadata

/**
 * Metadata extensions for an OPDS 2.0 catalog.
 */

data class OPDS20CatalogMetadata(
  val isProduction: Boolean,
  val isAutomatic: Boolean
) : OPDS20ExtensionElementType
