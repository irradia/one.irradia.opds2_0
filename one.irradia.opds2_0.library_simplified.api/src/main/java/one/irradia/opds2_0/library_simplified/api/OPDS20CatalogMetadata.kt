package one.irradia.opds2_0.library_simplified.api

import one.irradia.opds2_0.api.OPDS20ExtensionElementType
import java.net.URI

/**
 * Metadata extensions for an OPDS 2.0 catalog.
 */

data class OPDS20CatalogMetadata(
  val isProduction: Boolean,
  val isAutomatic: Boolean,
  val adobeVendorId: String?,
  val id: URI?
) : OPDS20ExtensionElementType
