package one.irradia.opds2_0.parser.extension.library_simplified

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.api.FRValueParserType
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.library_simplified.api.OPDS20Catalog
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogList
import one.irradia.opds2_0.parser.extension.spi.OPDS20FeedRoleExtensionType

class OPDS20CatalogExtension : OPDS20FeedRoleExtensionType<OPDS20CatalogList> {

  override fun createObjectFieldSchema(
    receiver: (OPDS20CatalogList) -> Unit
  ): FRParserObjectFieldSchema<OPDS20CatalogList> {
    return FRParserObjectFieldSchema(
      name = "catalogs",
      parser = { this.createParser(receiver) },
      isOptional = true
    )
  }

  private fun createParser(
    receiver: (OPDS20CatalogList) -> Unit
  ): FRValueParserType<OPDS20CatalogList> {
    val catalogs =
      mutableListOf<OPDS20Catalog>()

    val arrayParser =
      FRValueParsers.forArrayMonomorphic(
        forEach = { OPDS20CatalogValueParser { _, catalog -> catalogs.add(catalog) } },
        receiver = catalogs::addAll
      )

    return arrayParser.map(::OPDS20CatalogList)
      .map { catalogList ->
        receiver.invoke(catalogList)
        catalogList
      }
  }

  override val name: String =
    "one.irradia.opds2_0.parser.extension.library_simplified"

  override val version: String =
    "1.0"
}
