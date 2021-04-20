package one.irradia.opds2_0.parser.extension.library_simplified.internal

import one.irradia.fieldrush.api.FRParserObjectFieldSchema
import one.irradia.fieldrush.vanilla.FRValueParsers
import one.irradia.opds2_0.library_simplified.api.OPDS20CatalogMetadata
import one.irradia.opds2_0.parser.extension.spi.OPDS20ExtensionType
import one.irradia.opds2_0.parser.extension.spi.OPDS20MetadataRoleExtensionType

/**
 * An extension that allows for adding extra fields to a "metadata" role.
 */

class OPDS20CatalogMetadataExtension : OPDS20MetadataRoleExtensionType<OPDS20CatalogMetadata> {

  private var isProduction: Boolean = false
  private var isAutomatic: Boolean = false

  override fun createCompositeFieldExtensionSchemas(
    extensions: List<OPDS20ExtensionType>
  ): OPDS20MetadataRoleExtensionType.ExtensionSchemas<OPDS20CatalogMetadata> {

    val isAutomaticSchema =
      FRParserObjectFieldSchema(
        name = "isAutomatic",
        parser = {
          FRValueParsers.forBoolean { this.isAutomatic = it }
        },
        isOptional = true
      )

    val isProductionSchema =
      FRParserObjectFieldSchema(
        name = "isProduction",
        parser = {
          FRValueParsers.forBoolean { this.isProduction = it }
        },
        isOptional = true
      )

    return OPDS20MetadataRoleExtensionType.ExtensionSchemas(
      objectFieldSchemas = listOf(
        isAutomaticSchema,
        isProductionSchema
      ),
      onCompletion = {
        OPDS20CatalogMetadata(
          isProduction = this.isProduction,
          isAutomatic = this.isAutomatic
        )
      }
    )
  }
}
