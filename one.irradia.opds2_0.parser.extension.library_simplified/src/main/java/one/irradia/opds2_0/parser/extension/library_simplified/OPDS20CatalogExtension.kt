package one.irradia.opds2_0.parser.extension.library_simplified

import one.irradia.opds2_0.parser.extension.library_simplified.internal.OPDS20CatalogMetadataExtension
import one.irradia.opds2_0.parser.extension.library_simplified.internal.OPDS20CatalogRoleExtension
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import one.irradia.opds2_0.parser.extension.spi.OPDS20FeedRoleExtensionType
import one.irradia.opds2_0.parser.extension.spi.OPDS20MetadataRoleExtensionType

class OPDS20CatalogExtension : OPDS20ExtensionType {

  override val name: String =
    "one.irradia.opds2_0.parser.extension.library_simplified"

  override val version: String =
    "1.0"

  override fun feedRoleExtensions(): List<OPDS20FeedRoleExtensionType<*>> =
    listOf(OPDS20CatalogRoleExtension())

  override fun metadataRoleExtension(): List<OPDS20MetadataRoleExtensionType<*>> =
    listOf(OPDS20CatalogMetadataExtension())
}
