package one.irradia.opds2_0.parser.extension.library_simplified.internal

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRValueParserType
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.library_simplified.api.OPDS20Catalog
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogList
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import one.irradia.opds2_0.parser.extension.spi.OPDS20FeedRoleExtensionType

/**
 * An extension that allows for parsing a new top-level "catalogs" role in a feed.
 */

class OPDS20CatalogRoleExtension : OPDS20FeedRoleExtensionType<OPDS20CatalogList> {

  override fun createObjectFieldSchema(
    extensions: List<OPDS20ExtensionType>,
    receiver: (OPDS20CatalogList) -> Unit
  ): FRParserObjectFieldSchema<OPDS20CatalogList> {
    return FRParserObjectFieldSchema(
      name = "catalogs",
      parser = { this.createParser(extensions, receiver) },
      isOptional = true
    )
  }

  private fun createParser(
    extensions: List<OPDS20ExtensionType>,
    receiver: (OPDS20CatalogList) -> Unit
  ): FRValueParserType<OPDS20CatalogList> {
    val catalogs =
      mutableListOf<OPDS20Catalog>()

    val arrayParser =
      FRValueParsers.forArrayMonomorphic(
        forEach = { OPDS20CatalogValueParser(extensions) { _, catalog -> catalogs.add(catalog) } },
        receiver = catalogs::addAll
      )

    return arrayParser.map(::OPDS20CatalogList)
      .map { catalogList ->
        receiver.invoke(catalogList)
        catalogList
      }
  }
}
