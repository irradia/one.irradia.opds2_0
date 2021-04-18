package one.irradia.opds2_0.library_simplified.api

import one.irradia.opds2_0.api.OPDS20ExtensionElementType

/**
 * An OPDS 2.0 catalog list.
 */

data class OPDS20CatalogList(
  val catalogs: List<OPDS20Catalog>
) : OPDS20ExtensionElementType
